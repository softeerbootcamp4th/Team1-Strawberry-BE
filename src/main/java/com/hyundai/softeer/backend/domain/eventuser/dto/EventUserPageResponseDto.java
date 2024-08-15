package com.hyundai.softeer.backend.domain.eventuser.dto;

import com.hyundai.softeer.backend.domain.user.dto.UserInfoDto;
import com.hyundai.softeer.backend.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;


@Data
@AllArgsConstructor
@Builder
public class EventUserPageResponseDto {

   private Integer totalPages;
   private List<EventUserInfos> eventUserInfos;
}