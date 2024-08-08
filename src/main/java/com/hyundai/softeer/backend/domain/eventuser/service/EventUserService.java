package com.hyundai.softeer.backend.domain.eventuser.service;

import com.hyundai.softeer.backend.domain.eventuser.dto.EventUserInfoDto;
import com.hyundai.softeer.backend.domain.eventuser.entity.EventUser;
import com.hyundai.softeer.backend.domain.eventuser.exception.EventUserNotFoundException;
import com.hyundai.softeer.backend.domain.eventuser.repository.EventUserRepository;
import com.hyundai.softeer.backend.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EventUserService {
    private final EventUserRepository eventUserRepository;

    @Transactional(readOnly = true)
    public EventUserInfoDto getEventUserInfo(User user, Long subEventId) {
        EventUser eventUser = eventUserRepository.findByUserIdAndSubEventId(user.getId(), subEventId)
                .orElseThrow(() -> new EventUserNotFoundException());

        return EventUserInfoDto.fromEntity(eventUser);
    }
}
