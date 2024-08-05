package com.hyundai.softeer.backend.domain.lottery.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RankDto {
    private Integer rank;
    private String name;
    private Double score;

    public RankDto(String name, Double score) {
        this.name = name;
        this.score = score;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    @Override
    public String toString() {
        return "RankDto{" +
                "rank=" + rank +
                ", name=" + name +
                ", score=" + score +
                '}';
    }
}
