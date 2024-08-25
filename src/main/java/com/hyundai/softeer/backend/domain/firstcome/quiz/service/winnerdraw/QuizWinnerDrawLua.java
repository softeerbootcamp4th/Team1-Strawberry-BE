package com.hyundai.softeer.backend.domain.firstcome.quiz.service.winnerdraw;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hyundai.softeer.backend.domain.eventuser.entity.EventUser;
import com.hyundai.softeer.backend.domain.firstcome.quiz.dto.QuizFirstComeSubmitResponseDto;
import com.hyundai.softeer.backend.domain.firstcome.quiz.dto.WinnerDto;
import com.hyundai.softeer.backend.domain.firstcome.quiz.entity.QuizFirstCome;
import com.hyundai.softeer.backend.domain.firstcome.quiz.repository.QuizFirstComeRepository;
import com.hyundai.softeer.backend.domain.prize.entity.Prize;
import com.hyundai.softeer.backend.domain.prize.repository.PrizeRepository;
import com.hyundai.softeer.backend.domain.subevent.entity.SubEvent;
import com.hyundai.softeer.backend.domain.subevent.repository.SubEventRepository;
import com.hyundai.softeer.backend.domain.user.entity.User;
import com.hyundai.softeer.backend.domain.user.repository.UserRepository;
import com.hyundai.softeer.backend.domain.winner.entity.Winner;
import com.hyundai.softeer.backend.global.aop.LogExecutionTime;
import com.hyundai.softeer.backend.global.exception.error.JsonParseException;
import com.hyundai.softeer.backend.global.exception.error.SQLRuntimeException;
import com.hyundai.softeer.backend.global.service.RedisKvService;
import com.hyundai.softeer.backend.global.service.RedisSetService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
public class QuizWinnerDrawLua implements QuizWinnerDraw {

    private final RedisTemplate<String, String> redisTemplate;
    private final RedisSetService redisSetService;
    private final RedisKvService redisKvService;
    private final ObjectMapper objectMapper;
    private final DataSource dataSource;
    private final String USER_COUNTER = "COUNTER";
    private final String WINNER_SET = "WINNER_SET";

    @Scheduled(fixedRate = 60000)
    @Transactional
    @LogExecutionTime
    protected void saveWinners() {
        Collection<String> allMembers = redisSetService.getAllMembers(WINNER_SET);

        List<WinnerDto> winnerDtos = new ArrayList<>();

        for (String memberJson : allMembers) {
            try {
                WinnerDto winnerDto = objectMapper.readValue(memberJson, WinnerDto.class);
                winnerDtos.add(winnerDto);

                if (winnerDtos.size() >= 500) {
                    processWinnerBatch(winnerDtos);
                    winnerDtos.clear();
                }
            } catch (JsonProcessingException e) {
                // 로그 기록 및 예외 처리
                System.err.println("Failed to parse JSON: " + memberJson);
            }
        }

        // 남은 데이터 처리
        if (!winnerDtos.isEmpty()) {
            processWinnerBatch(winnerDtos);
        }

        // 처리 완료된 데이터 Redis에서 삭제
        redisSetService.removeAll(WINNER_SET);
        redisKvService.setKeyValue(USER_COUNTER, "0");
    }

    protected void processWinnerBatch(List<WinnerDto> winnerDtos) {
        String sql = "INSERT INTO winners (prize_id, sub_event_id, user_id, ranking) VALUES (?, ?, ?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            conn.setAutoCommit(false);

            for (int i = 0; i < winnerDtos.size(); i++) {
                WinnerDto winner = winnerDtos.get(i);
                pstmt.setLong(1, winner.getPrizeId());
                pstmt.setLong(2, winner.getSubEventId());
                pstmt.setLong(3, winner.getUserId());
                pstmt.setInt(4, winner.getRanking());
                pstmt.addBatch();

                if (i % 500 == 0 || i == winnerDtos.size() - 1) {
                    pstmt.executeBatch();
                    conn.commit();
                }
            }
        } catch (SQLException e) {
            throw new SQLRuntimeException();
        }
    }

    @LogExecutionTime
    private Long drawWinnerSync(long maxWinners, long userId, long subEventId, long prizeId) {
        String script =
                "local current_count = redis.call('GET', KEYS[1]) " +
                        "if current_count == false then " +
                        "    current_count = 0 " +
                        "    redis.call('SET', KEYS[1], current_count) " +
                        "else " +
                        "    current_count = tonumber(current_count) " +
                        "end " +

                        "if current_count >= tonumber(ARGV[1]) then " +
                        "    return 0 " + // 최대 당첨자 수 초과
                        "end " +

                        "if redis.call('EXISTS', KEYS[2]) == 0 then " +
                        "    redis.call('SADD', KEYS[2], 'dummy') " +  // Set 생성
                        "    redis.call('SREM', KEYS[2], 'dummy') " +  // dummy 값 제거
                        "end " +

                        "if redis.call('SISMEMBER', KEYS[2], ARGV[2]) == 1 then " +
                        "    return -1 " + // 이미 당첨된 사용자
                        "end " +

                        "local new_count = redis.call('INCR', KEYS[1]) " +

                        "redis.call('SADD', KEYS[2], ARGV[2]) " +
                        "return 1"; // 새로운 당첨자

        WinnerDto winnerDto = new WinnerDto(userId, subEventId, prizeId, 0);
        String winnerDtoRaw = null;

        try {
            winnerDtoRaw = objectMapper.writeValueAsString(winnerDto);
        } catch (JsonProcessingException e) {
            throw new JsonParseException();
        }

        return redisTemplate.execute(new DefaultRedisScript<>(script, Long.class),
                List.of(USER_COUNTER, WINNER_SET),
                String.valueOf(maxWinners),
                String.valueOf(winnerDtoRaw));
    }

    @Override
    @LogExecutionTime
    public QuizFirstComeSubmitResponseDto winnerDraw(EventUser eventUser, QuizFirstCome quizFirstCome, SubEvent subEvent, User authenticatedUser) {

        String prizeResultImgUrl = quizFirstCome.getPrize().getPrizeResultImgUrl();

        Long l = drawWinnerSync(quizFirstCome.getWinners(), authenticatedUser.getId(), subEvent.getId(), quizFirstCome.getPrize().getId());

        if(l == -1L) {
           return QuizFirstComeSubmitResponseDto.correctBut();
        }

        if(l == 0L) {
           return QuizFirstComeSubmitResponseDto.alreadyParticipant();
        }

        return QuizFirstComeSubmitResponseDto.winner(prizeResultImgUrl);
    }
}
