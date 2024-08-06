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
        Optional<List<Winner>> bySubEventId = winnerRepository.findBySubEventId(subEventId);
        if (bySubEventId.isPresent() && !bySubEventId.get().isEmpty()) {
            throw new AlreadyDrawedException();
        }

        DrawingLotteryEvent drawingLotteryEvent = drawingLotteryRepository.findById(subEventId)
                .orElseThrow(() -> new DrawingNotFoundException());

        Map<Integer, WinnerInfo> winnersMeta = ParseUtil.parseWinnersMeta(drawingLotteryEvent.getWinnersMeta());

        int lotteryWinnerCount = 3;

        Pageable pageable = PageRequest.of(0, lotteryWinnerCount);
        List<EventUser> nByRand = eventUserRepository.findNByRand(subEventId, lotteryWinnerCount, pageable);

        if (nByRand.size() < lotteryWinnerCount) {
            lotteryWinnerCount -= nByRand.size();
            pageable = PageRequest.of(0, lotteryWinnerCount);
            List<EventUser> nByRand2 = eventUserRepository.findRestByRand(subEventId, pageable);
        }

        LotteryScoreWeight scoreWeight = new LotteryScoreWeight(1.0, 1.0, 1.0, 1.0);

        List<WinnerCandidate> winnerCandidates = getWinners(winnersMeta, nByRand, scoreWeight)
                .stream()
                .sorted(Comparator.comparingDouble(WinnerCandidate::getRandomValue).reversed())
                .toList();

        int rank = 1;
        int count = 0;

        List<Winner> winners = new ArrayList<>();

        // 각 rank에 해당하는 WinnerInfo를 가져옵니다.
        for (Map.Entry<Integer, WinnerInfo> entry : winnersMeta.entrySet()) {
            int winnerCount = entry.getValue().getWinnerCount();

            // 상위 N명에 대해 1등으로 설정
            while (count < winnerCount && count < winnerCandidates.size()) {
                WinnerCandidate winnerCandidate = winnerCandidates.get(count);
                Winner winner = Winner.builder()
                        .user(userRepository.getReferenceById(winnerCandidate.getUserId()))
                        .subEvent(subEventRepository.getReferenceById(subEventId))
                        .prize(prizeRepository.getReferenceById(winnersMeta.get(rank).getPrizeId()))
                        .build();

                winners.add(winner);
                count++;
            }

            // 다음 rank로 이동합니다.
            rank++;
        }

        winnerRepository.saveAll(winners);

        return winners;
    }
}
