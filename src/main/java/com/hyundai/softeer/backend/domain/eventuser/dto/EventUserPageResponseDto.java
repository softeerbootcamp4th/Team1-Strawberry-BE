package com.hyundai.softeer.backend.domain.eventuser.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class EventUserPageResponseDto {

    private int totalPages;
    private List<UserInfoDtoWithIsWinner> users;
}
