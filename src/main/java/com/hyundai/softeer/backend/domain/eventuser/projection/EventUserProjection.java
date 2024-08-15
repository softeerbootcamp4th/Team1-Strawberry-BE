package com.hyundai.softeer.backend.domain.eventuser.projection;

import java.time.LocalDate;

public interface EventUserProjection {
    String getName();
    LocalDate getBirthDate();
    String getPhoneNumber();
    String getEmail();
    Integer getIsWinner();
}
