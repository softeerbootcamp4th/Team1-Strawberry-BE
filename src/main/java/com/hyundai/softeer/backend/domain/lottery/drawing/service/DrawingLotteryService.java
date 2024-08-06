package com.hyundai.softeer.backend.domain.lottery.drawing.service;

import com.hyundai.softeer.backend.domain.eventuser.entity.EventUser;
import com.hyundai.softeer.backend.domain.eventuser.repository.EventUserRepository;
import com.hyundai.softeer.backend.domain.lottery.drawing.dto.DrawingLotteryLandDto;
import com.hyundai.softeer.backend.domain.lottery.drawing.entity.DrawingLotteryEvent;
import com.hyundai.softeer.backend.domain.lottery.drawing.exception.DrawingNotFoundException;
import com.hyundai.softeer.backend.domain.lottery.drawing.repository.DrawingLotteryRepository;
import com.hyundai.softeer.backend.domain.lottery.dto.RankDto;
import com.hyundai.softeer.backend.domain.lottery.exception.AlreadyDrawedException;
import com.hyundai.softeer.backend.domain.lottery.service.LotteryService;
import com.hyundai.softeer.backend.domain.prize.repository.PrizeRepository;
import com.hyundai.softeer.backend.domain.subevent.dto.LotteryScoreWeight;
import com.hyundai.softeer.backend.domain.subevent.dto.WinnerCandidate;
import com.hyundai.softeer.backend.domain.subevent.dto.WinnerInfo;
import com.hyundai.softeer.backend.domain.subevent.entity.SubEvent;
import com.hyundai.softeer.backend.domain.subevent.enums.SubEventType;
import com.hyundai.softeer.backend.domain.subevent.repository.SubEventRepository;
import com.hyundai.softeer.backend.domain.user.repository.UserRepository;
import com.hyundai.softeer.backend.domain.winner.entity.Winner;
import com.hyundai.softeer.backend.domain.winner.repository.WinnerRepository;
import com.hyundai.softeer.backend.global.utils.ParseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class DrawingLotteryService implements LotteryService {
    public static final int MULTIPLE_WINNER_COUNT = 3;
    private final SubEventRepository subEventRepository;
    private final EventUserRepository eventUserRepository;
    private final DrawingLotteryRepository drawingLotteryRepository;
    private final WinnerRepository winnerRepository;
    private final UserRepository userRepository;
    private final PrizeRepository prizeRepository;

    @Transactional(readOnly = true)
    public DrawingLotteryLandDto getDrawingLotteryLand(long eventId) {
        List<SubEvent> events = subEventRepository.findByEventId(eventId);
        SubEvent drawing = findDrawingEvent(events);

        if (drawing == null) {
            throw new DrawingNotFoundException();
        }

        return DrawingLotteryLandDto.fromEntity(drawing);
    }

    private SubEvent findDrawingEvent(List<SubEvent> subEvents) {
        for (SubEvent subEvent : subEvents) {
            if (subEvent.getEventType().equals(SubEventType.DRAWING)) {
                return subEvent;
            }
        }
        return null;
    }

    @Transactional(readOnly = true)
    @Override
    public List<RankDto> getRankList(long subEventId, int rankCount) {
        Pageable pageable = PageRequest.of(0, rankCount);
        List<RankDto> topNBySubEventId = eventUserRepository.findTopNBySubEventId(subEventId, pageable);

        for (int i = 0; i < topNBySubEventId.size(); i++) {
            topNBySubEventId.get(i).setRank(i + 1);
        }

        log.info("topNBySubEventId: {}", topNBySubEventId);

        return topNBySubEventId;
    }

    @Transactional
    public List<WinnerCandidate> drawWinner(long subEventId) {
        // subEventId 유효성 검사
        DrawingLotteryEvent drawingLotteryEvent = drawingLotteryRepository.findById(subEventId)
                .orElseThrow(() -> new DrawingNotFoundException());

        // 이미 추첨이 되었는지 확인
        Optional<List<Winner>> bySubEventId = winnerRepository.findBySubEventId(subEventId);
        if (bySubEventId.isPresent() && !bySubEventId.get().isEmpty()) {
            throw new AlreadyDrawedException();
        }

        // 당첨자 정보 파싱
        Map<Integer, WinnerInfo> winnersMeta = ParseUtil.parseWinnersMeta(drawingLotteryEvent.getWinnersMeta());

        // TODO: Score 자체 변동사항으로 인한 임시 코드
        LotteryScoreWeight scoreWeight = new LotteryScoreWeight(1.0, 1.0, 1.0, 1.0);

        int totalWinners = winnersMeta.values().stream()
                .mapToInt(WinnerInfo::getWinnerCount)
                .sum();

        // N 배수 당첨자 수
        int lotteryWinnerCount = totalWinners * MULTIPLE_WINNER_COUNT;

        // 랜덤 사용자를 추출할 기준 값
        int randomValue = getRandomValue(subEventId);

        // 랜덤 사용자 추출
        Pageable pageable = PageRequest.of(0, lotteryWinnerCount);
        List<EventUser> randomEventUsers = eventUserRepository.findNByRand(subEventId, randomValue, pageable);

        // 랜덤 사용자가 부족할 경우 나머지 사용자를 추가로 추출
        if (randomEventUsers.size() < lotteryWinnerCount) {
            lotteryWinnerCount -= randomEventUsers.size();
            pageable = PageRequest.of(0, lotteryWinnerCount);
            List<EventUser> extraEventUsers = eventUserRepository.findRestByRand(subEventId, pageable);

            randomEventUsers.addAll(extraEventUsers);
        }

        // 당첨자 후보 선정
        List<WinnerCandidate> winnerCandidates = getWinners(randomEventUsers, scoreWeight, totalWinners)
                .stream()
                .sorted(Comparator.comparingDouble(WinnerCandidate::getRandomValue).reversed())
                .toList();

        // 당첨자 선정
        int rank = 1;
        int index = 0;

        List<Winner> winners = new ArrayList<>();

        for (Map.Entry<Integer, WinnerInfo> entry : winnersMeta.entrySet()) {
            int count = 0;
            int winnerCount = entry.getValue().getWinnerCount();

            while (count < winnerCount && count < winnerCandidates.size()) {
                WinnerCandidate winnerCandidate = winnerCandidates.get(index);
                Winner winner = Winner.builder()
                        .user(userRepository.getReferenceById(winnerCandidate.getUserId()))
                        .subEvent(subEventRepository.getReferenceById(subEventId))
                        .prize(prizeRepository.getReferenceById(winnersMeta.get(rank).getPrizeId()))
                        .rank(rank)
                        .build();

                winners.add(winner);
                count++;
                index++;
            }

            rank++;
        }

        // 당첨자 저장
        winnerRepository.saveAll(winners);

        return winnerCandidates;
    }

    private int getRandomValue(long subEventId) {
        long randomSeed = System.currentTimeMillis();
        Random random = new Random(randomSeed);

        long left = eventUserRepository.countBySubEventId(subEventId);
        return random.nextInt((int) left) + 1;
    }
}
