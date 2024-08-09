package com.hyundai.softeer.backend.domain.subevent.service;

import com.hyundai.softeer.backend.domain.eventuser.entity.EventUser;
import com.hyundai.softeer.backend.domain.eventuser.repository.EventUserRepository;
import com.hyundai.softeer.backend.domain.lottery.drawing.exception.DrawingNotFoundException;
import com.hyundai.softeer.backend.domain.lottery.drawing.service.DrawingLotteryService;
import com.hyundai.softeer.backend.domain.lottery.exception.AlreadyDrawedException;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class SubEventService {
    public static final int MULTIPLE_WINNER_COUNT = 3;

    private final EventUserRepository eventUserRepository;
    private final WinnerRepository winnerRepository;
    private final UserRepository userRepository;
    private final SubEventRepository subEventRepository;
    private final PrizeRepository prizeRepository;
    private final DrawingLotteryService drawingLotteryService;

    @Transactional
    public List<WinnerCandidate> drawWinner(long subEventId) {
        SubEvent subEvent = validateSubEventId(subEventId);

        // 당첨자 정보 파싱
        Map<Integer, WinnerInfo> winnersMeta = ParseUtil.parseWinnersMeta(subEvent.getWinnersMeta());

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
        List<EventUser> randomEventUsers = eventUserRepository.findNByRand(subEventId, randomValue, lotteryWinnerCount);

        // 랜덤 사용자가 부족할 경우 나머지 사용자를 추가로 추출
        if (randomEventUsers.size() < lotteryWinnerCount) {
            lotteryWinnerCount -= randomEventUsers.size();
            List<EventUser> extraEventUsers = eventUserRepository.findRestByRand(subEventId, lotteryWinnerCount);

            randomEventUsers.addAll(extraEventUsers);
        }

        // 당첨자 후보 선정
        List<WinnerCandidate> winnerCandidates = drawingLotteryService.getWinners(randomEventUsers, scoreWeight, totalWinners)
                .stream()
                .sorted(Comparator.comparingDouble(WinnerCandidate::getRandomValue).reversed())
                .toList();

        // 당첨자 선정
        int rank = 1;
        int index = 0;

        List<Winner> winners = new ArrayList<>();
        List<WinnerCandidate> winnersInfo = new ArrayList<>();

        for (Map.Entry<Integer, WinnerInfo> entry : winnersMeta.entrySet()) {
            int count = 0;
            int winnerCount = entry.getValue().getWinnerCount();

            while (count < winnerCount && count < winnerCandidates.size()) {
                WinnerCandidate winnerCandidate = winnerCandidates.get(index);
                Winner winner = Winner.builder()
                        .user(userRepository.getReferenceById(winnerCandidate.getUserId()))
                        .subEvent(subEventRepository.getReferenceById(subEventId))
                        .prize(prizeRepository.getReferenceById(winnersMeta.get(rank).getPrizeId()))
                        .ranking(rank)
                        .build();

                winners.add(winner);
                winnersInfo.add(winnerCandidate);
                count++;
                index++;
            }

            rank++;
        }

        // 당첨자 저장
        winnerRepository.saveAll(winners);

        return winnersInfo;
    }

    private SubEvent validateSubEventId(long subEventId) {
        // subEventId 유효성 검사
        SubEvent subEvent = subEventRepository.findById(subEventId)
                .orElseThrow(() -> new DrawingNotFoundException());

        if (!subEvent.getEventType().equals(SubEventType.DRAWING)) {
            throw new DrawingNotFoundException();
        }

        // 이미 추첨이 되었는지 확인
        Optional<List<Winner>> bySubEventId = winnerRepository.findBySubEventId(subEventId);
        if (bySubEventId.isPresent() && !bySubEventId.get().isEmpty()) {
            throw new AlreadyDrawedException();
        }
        return subEvent;
    }

    private int getRandomValue(long subEventId) {
        long randomSeed = System.currentTimeMillis();
        Random random = new Random(randomSeed);

        long left = eventUserRepository.findMaxBySubEventId(subEventId);
        return random.nextInt((int) left) + 1;
    }
}
