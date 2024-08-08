package com.hyundai.softeer.backend.domain.eventuser.service;

import com.hyundai.softeer.backend.domain.eventuser.dto.EventUserInfoDto;
import com.hyundai.softeer.backend.domain.eventuser.dto.SharedUrlDto;
import com.hyundai.softeer.backend.domain.eventuser.entity.EventUser;
import com.hyundai.softeer.backend.domain.eventuser.exception.EventUserNotFoundException;
import com.hyundai.softeer.backend.domain.eventuser.exception.NonPlayedUserException;
import com.hyundai.softeer.backend.domain.eventuser.repository.EventUserRepository;
import com.hyundai.softeer.backend.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EventUserService {
    public static final int SHARED_URL_LENGTH = 6;
    private final EventUserRepository eventUserRepository;

    @Transactional(readOnly = true)
    public EventUserInfoDto getEventUserInfo(User user, Long subEventId) {
        EventUser eventUser = eventUserRepository.findByUserIdAndSubEventId(user.getId(), subEventId)
                .orElseThrow(() -> new EventUserNotFoundException());

        return EventUserInfoDto.fromEntity(eventUser);
    }

    public SharedUrlDto getSharedUrl(User user, long subEventId) {

        EventUser eventUser = eventUserRepository.findByUserIdAndSubEventId(user.getId(), subEventId)
                .orElseThrow(() -> new EventUserNotFoundException());

        String sharedUrl = eventUser.getSharedUrl();
        if (sharedUrl.isBlank()) {
            sharedUrl = RandomStringUtils.random(SHARED_URL_LENGTH, true, true);
        }

        double gameScore = eventUser.getGameScore();

        if (gameScore <= 0.0) {
            throw new NonPlayedUserException();
        }

        return new SharedUrlDto(sharedUrl, gameScore);
    }
}
