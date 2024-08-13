package com.hyundai.softeer.backend.domain.lottery.drawing.service;

import com.hyundai.softeer.backend.domain.lottery.drawing.dto.PositionDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScoreCalculator {
    public double calculateAverageEuclideanDistance(List<PositionDto> userPoints, List<PositionDto> answerPoints) {
        double totalScore = 0.0;
        int count = 0;
        double maxDistance = 100 * Math.sqrt(2); // 최대 거리 계산

        for (PositionDto answerPoint : answerPoints) {
            double minDistance = Double.MAX_VALUE;

            // 각 answerPoint에 대해 userPoints의 최소 거리 계산
            for (PositionDto userPoint : userPoints) {
                double distance = Math.sqrt(Math.pow(answerPoint.getX() - userPoint.getX(), 2) + Math.pow(answerPoint.getY() - userPoint.getY(), 2));
                minDistance = Math.min(minDistance, distance);
            }

            // 점수 계산
            double score;
            if (minDistance >= maxDistance) {
                score = 0; // 최대 거리 이상일 경우 0점
            } else if (minDistance == 0) {
                score = 100; // 완벽히 일치할 경우 100점
            } else {
                score = 100 * (1 - (minDistance / maxDistance)); // 점수 계산
            }

            totalScore += score; // 전체 점수에 추가
            count++;
        }

        return count == 0 ? 0 : totalScore / count; // count가 0일 경우 0 반환
    }

    public double calculateAverageCosineSimilarity(List<PositionDto> userPoints, List<PositionDto> answerPoints) {
        double totalSimilarity = 0;
        int count = 0;

        for (PositionDto points : answerPoints) {
            double maxSimilarity = -1;
            for (PositionDto userPoint : userPoints) {
                double similarity = (points.getX() * userPoint.getX() + points.getY() * userPoint.getY()) /
                        (Math.sqrt(Math.pow(points.getX(), 2) + Math.pow(points.getY(), 2)) * Math.sqrt(Math.pow(userPoint.getX(), 2) + Math.pow(userPoint.getY(), 2)));
                maxSimilarity = Math.max(maxSimilarity, similarity);
            }
            double scaledScore = ((maxSimilarity + 1) / 2) * 100;
            totalSimilarity += scaledScore;
            count++;
        }

        return totalSimilarity / count;
    }
}
