package com.hyundai.softeer.backend.domain.lottery.drawing.service;

import com.hyundai.softeer.backend.domain.lottery.drawing.dto.PositionDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScoreCalculator {
    public double calculateAverageEuclideanDistance(List<PositionDto> userPoints, List<PositionDto> answerPoints) {
        double totalScore = 0.0;
        int count = 0;

        // 최대 거리를 줄여 점수를 감소시킵니다.
        double maxDistance = 20 * Math.sqrt(2); // 예: 최대 거리 조정

        for (PositionDto answerPoint : answerPoints) {
            double minDistance = Double.MAX_VALUE;

            // 각 answerPoint에 대해 userPoints의 최소 거리 계산
            for (PositionDto userPoint : userPoints) {
                double distance = getDistance(answerPoint, userPoint);
                minDistance = Math.min(minDistance, distance);
            }

            // 점수 계산 (감쇠 비율 조정)
            double score;
            if (minDistance >= maxDistance) {
                score = 0; // 최대 거리 이상일 경우 0점
            } else if (minDistance == 0) {
                score = 100; // 완벽히 일치할 경우 100점
            } else {
                score = 100 * Math.pow(1 - (minDistance / maxDistance), 0.3); // 감쇠 비율 조정
            }

            totalScore += score; // 전체 점수에 추가
            count++;
        }

        return count == 0 ? 0 : totalScore / count; // count가 0일 경우 0 반환
    }


    private double getDistance(PositionDto answerPoint, PositionDto userPoint) {
        double distance = Math.sqrt(Math.pow(answerPoint.getX() - userPoint.getX(), 2) + Math.pow(answerPoint.getY() - userPoint.getY(), 2));
        return distance;
    }

    public double calculateAverageCosineSimilarity(List<PositionDto> userPoints, List<PositionDto> answerPoints) {
        double totalSimilarity = 0;
        int count = 0;

        for (PositionDto points : answerPoints) {
            double maxSimilarity = -1;
            for (PositionDto userPoint : userPoints) {
                double similarity = getSimilarity(points, userPoint);
                maxSimilarity = Math.max(maxSimilarity, similarity);
            }
            double scaledScore = ((maxSimilarity + 1) / 2) * 100;
            totalSimilarity += scaledScore;
            count++;
        }

        return totalSimilarity / count;
    }

    private double getSimilarity(PositionDto points, PositionDto userPoint) {
        double similarity = (points.getX() * userPoint.getX() + points.getY() * userPoint.getY()) /
                (Math.sqrt(Math.pow(points.getX(), 2) + Math.pow(points.getY(), 2)) * Math.sqrt(Math.pow(userPoint.getX(), 2) + Math.pow(userPoint.getY(), 2)));
        return similarity;
    }
}
