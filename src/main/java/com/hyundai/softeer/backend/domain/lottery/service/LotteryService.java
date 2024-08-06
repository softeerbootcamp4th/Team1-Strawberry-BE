package com.hyundai.softeer.backend.domain.lottery.service;

import com.hyundai.softeer.backend.domain.eventuser.entity.EventUser;
import com.hyundai.softeer.backend.domain.lottery.dto.RankDto;
import com.hyundai.softeer.backend.domain.subevent.dto.LotteryScoreWeight;
import com.hyundai.softeer.backend.domain.subevent.dto.WinnerCandidate;
import com.hyundai.softeer.backend.domain.subevent.dto.WinnerInfo;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
public interface LotteryService {

    /**
     * 당첨자 선정 메서드
     *
     * @param winnersMeta 당첨자 메타 데이터
     * @param eventUsers  1차 랜덤 선정된 참여자 리스트
     * @param scoreWeight 각 스코어에 대한 가중치
     * @return 당첨자 리스트
     */
    default List<WinnerCandidate> getWinners(Map<Integer, WinnerInfo> winnersMeta, List<EventUser> eventUsers, LotteryScoreWeight scoreWeight) {
        int totalWinners = winnersMeta.values().stream()
                .mapToInt(WinnerInfo::getWinnerCount)
                .sum();

        List<WinnerCandidate> candidates = new java.util.ArrayList<>(eventUsers.stream()
                .map(eventUser -> {
                    double eventRandomScore = Math.random();
                    eventRandomScore += calculateWeightedValue(eventUser, scoreWeight);
                    return new WinnerCandidate(eventUser.getId(), eventRandomScore);
                })
                .toList());

        // 당첨자들을 난수에 따라 정렬
        candidates.sort(Comparator.comparingDouble(WinnerCandidate::getRandomValue).reversed());

        // 최종 당첨자 리스트 (예: 상위 N 명)
        return candidates.stream()
                .limit(totalWinners)
                .toList();
    }

    private double calculateWeightedValue(EventUser eventUser, LotteryScoreWeight scoreWeight) {
        double sharedScore = eventUser.getSharedScore();
        double priorityScore = eventUser.getPriorityScore();
        double lottoScore = eventUser.getLottoScore();
        double gameScore = eventUser.getGameScore();

        return (sharedScore * scoreWeight.getSharedWeight()) +
                (priorityScore * scoreWeight.getPriorityWeight()) +
                (lottoScore * scoreWeight.getLottoWeight()) +
                (gameScore * scoreWeight.getGameWeight());
    }

    List<RankDto> getRankList(Long subEventId, int rankCount);
}
