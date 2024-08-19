package com.hyundai.softeer.backend.domain.eventuser.projection;

import com.hyundai.softeer.backend.domain.eventuser.entity.EventUser;
import com.hyundai.softeer.backend.domain.user.entity.User;

public interface EventUserPageProjection {

    User getUser();
    Boolean getIsWinner();
}
