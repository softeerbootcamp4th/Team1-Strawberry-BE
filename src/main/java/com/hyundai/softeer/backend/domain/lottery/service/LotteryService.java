package com.hyundai.softeer.backend.domain.lottery.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hyundai.softeer.backend.domain.eventuser.entity.EventUser;
import com.hyundai.softeer.backend.domain.subevent.dto.LotteryScoreWeight;
import com.hyundai.softeer.backend.domain.subevent.dto.WinnerCandidate;
import com.hyundai.softeer.backend.domain.subevent.dto.WinnerInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class LotteryService {

    /**
     * 당첨자 선정 메서드
     *
     * @param winnersMeta 당첨자 메타 데이터
     * @param eventUsers  1차 랜덤 선정된 참여자 리스트
     * @param scoreWeight 각 스코어에 대한 가중치
     * @return 당첨자 리스트
     */
    public List<WinnerCandidate> getWinners(Map<Integer, WinnerInfo> winnersMeta, List<EventUser> eventUsers, LotteryScoreWeight scoreWeight) {
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

    private Map<Integer, WinnerInfo> parseWinnersMeta(String winnersMeta) {
        Map<Integer, WinnerInfo> resultMap = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // JSON 문자열을 JsonNode로 변환
            JsonNode rootNode = objectMapper.readTree(winnersMeta.replace("'", "\""));

            // 각 키와 값 파싱
            rootNode.fields().forEachRemaining(entry -> {
                Integer rank = Integer.valueOf(entry.getKey());
                JsonNode values = entry.getValue();

                int winnerCount = values.get(0).asInt();
                int prizeId = values.get(1).asInt();

                resultMap.put(rank, new WinnerInfo(winnerCount, prizeId));
            });

        } catch (IOException e) {
            // TODO 예외 처리
            e.printStackTrace();
        }
        return resultMap;
    }

}