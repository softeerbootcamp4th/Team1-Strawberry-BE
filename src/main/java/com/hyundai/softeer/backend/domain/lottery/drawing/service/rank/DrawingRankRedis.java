package com.hyundai.softeer.backend.domain.lottery.drawing.service.rank;

import com.hyundai.softeer.backend.domain.eventuser.entity.EventUser;
import com.hyundai.softeer.backend.domain.eventuser.exception.EventUserNotFoundException;
import com.hyundai.softeer.backend.domain.eventuser.repository.EventUserRepository;
import com.hyundai.softeer.backend.domain.lottery.drawing.dto.DrawingTotalScoreDto;
import com.hyundai.softeer.backend.domain.lottery.dto.RankDto;
import com.hyundai.softeer.backend.domain.subevent.dto.SubEventRequest;
import com.hyundai.softeer.backend.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class DrawingRankRedis implements DrawingRank {
    private final EventUserRepository eventUserRepository;
    private final RedisTemplate<String, String> redisTemplate;

    public static final String FIRST_GAME_SCORE = "1_game_score";
    public static final String SECOND_GAME_SCORE = "2_game_score";
    public static final String THIRD_GAME_SCORE = "3_game_score";
    public static final String GAME_SCORE = "game_score";
    public static final List<Double> SCORE_WEIGHTS = List.of(0.25, 0.35, 0.4);
    private static final String RANKING_KEY = "ranking";

    @Transactional(readOnly = true)
    @Override
    public List<RankDto> getRankList(SubEventRequest subEventRequest, int rankCount) {
        Set<ZSetOperations.TypedTuple<String>> rankedUsers = redisTemplate.opsForZSet().reverseRangeWithScores(RANKING_KEY, 0, rankCount - 1);
        if (rankedUsers == null) {
            return List.of();  // 빈 리스트 반환
        }
        List<RankDto> rankList = new ArrayList<>();
        int rank = 1;

        for (ZSetOperations.TypedTuple<String> tuple : rankedUsers) {
            RankDto rankDto = new RankDto(tuple.getValue(), tuple.getScore());
            rankDto.setRank(rank++);
            rankList.add(rankDto);
        }

        return rankList;
    }

    @Transactional
    @Override
    public DrawingTotalScoreDto getDrawingTotalScore(User authenticatedUser, SubEventRequest subEventRequest) {
        EventUser eventUser = eventUserRepository.findByUserIdAndSubEventId(authenticatedUser.getId(), subEventRequest.getSubEventId())
                .orElseThrow(() -> new EventUserNotFoundException());

        Map<String, Object> scores = eventUser.getScores();

        double firstScore = (double) scores.getOrDefault(FIRST_GAME_SCORE, 0.0);
        double secondScore = (double) scores.getOrDefault(SECOND_GAME_SCORE, 0.0);
        double thirdScore = (double) scores.getOrDefault(THIRD_GAME_SCORE, 0.0);

        double totalScore = firstScore * SCORE_WEIGHTS.get(0) + secondScore * SCORE_WEIGHTS.get(1) + thirdScore * SCORE_WEIGHTS.get(2);
        double maxScore = (double) scores.getOrDefault(GAME_SCORE, 0.0);

        if (totalScore > maxScore) {
            maxScore = totalScore;
            eventUser.updateScores(GAME_SCORE, totalScore);
            eventUser.updateGameScore(totalScore);
            eventUserRepository.save(eventUser);
            redisTemplate.opsForZSet().add(RANKING_KEY, authenticatedUser.getName(), totalScore);
        }

        return DrawingTotalScoreDto.builder()
                .totalScore(totalScore)
                .maxScore(maxScore)
                .chance(eventUser.getChance())
                .expectationBonusChance(eventUser.getExpectationBonusChance())
                .shareBonusChance(eventUser.getShareBonusChance())
                .build();
    }

    @Transactional
    public void updateRankingData(long subEventId, int rankCount) {
        Pageable pageable = PageRequest.of(0, rankCount);
        List<RankDto> topNBySubEventId = eventUserRepository.findTopNBySubEventId(subEventId, pageable);

        ZSetOperations<String, String> zSetOps = redisTemplate.opsForZSet();

        zSetOps.removeRange(RANKING_KEY, 0, -1);

        for (RankDto rankDto : topNBySubEventId) {
            zSetOps.add(RANKING_KEY, rankDto.getName(), rankDto.getScore());
        }
    }
}
