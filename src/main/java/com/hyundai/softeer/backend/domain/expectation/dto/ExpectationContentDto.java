package com.hyundai.softeer.backend.domain.expectation.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class ExpectationContentDto {
    private final String name;
    private final String expectationComment;
}
