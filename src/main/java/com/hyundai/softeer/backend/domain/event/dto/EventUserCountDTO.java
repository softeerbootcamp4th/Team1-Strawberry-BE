package com.hyundai.softeer.backend.domain.event.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class EventUserCountDTO {

    private Long eventId;
    private Long totalUsers;

    public static List<EventUserCountDTO> fromObjects(List<Object[]> results) {
        return results.stream()
                .map(result -> new EventUserCountDTO(
                        ((Number) result[0]).longValue(),
                        ((Number) result[1]).longValue()))
                .collect(Collectors.toList());
    }
}
