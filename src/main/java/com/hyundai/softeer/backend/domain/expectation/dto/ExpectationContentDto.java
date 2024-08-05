package com.hyundai.softeer.backend.domain.expectation.dto;

import lombok.*;

@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Getter
@Setter
public class ExpectationContentDto {
    private final String name;
    private final String expectationComment;
}
