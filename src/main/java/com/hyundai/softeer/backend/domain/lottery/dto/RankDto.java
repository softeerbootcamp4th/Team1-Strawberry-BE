package com.hyundai.softeer.backend.domain.lottery.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class RankDto {
    @Schema(example = "1")
    private Integer rank;

    @Schema(example = "홍길동")
    private String name;

    @Schema(example = "100.0")
    private Double score;

    public RankDto(String name, Double score) {
        this.name = name;
        this.score = score;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }
}
