package com.hyundai.softeer.backend.domain.expectation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class ExpectationsResponseDto {

    private int totalPages;
    private List<ExpectationContentDto> comments;
}
