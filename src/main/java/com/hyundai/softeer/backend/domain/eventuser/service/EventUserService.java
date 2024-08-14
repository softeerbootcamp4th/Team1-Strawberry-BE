package com.hyundai.softeer.backend.domain.eventuser.service;

import com.hyundai.softeer.backend.domain.eventuser.dto.EventUserInfoDto;
import com.hyundai.softeer.backend.domain.eventuser.dto.RedirectUrlDto;
import com.hyundai.softeer.backend.domain.eventuser.dto.SharedUrlDto;
import com.hyundai.softeer.backend.domain.eventuser.dto.SharedUrlRequest;
import com.hyundai.softeer.backend.domain.eventuser.entity.EventUser;
import com.hyundai.softeer.backend.domain.eventuser.exception.EventUserNotFoundException;
import com.hyundai.softeer.backend.domain.eventuser.exception.NonPlayedUserException;
import com.hyundai.softeer.backend.domain.eventuser.exception.SharedUrlNotFoundException;
import com.hyundai.softeer.backend.domain.eventuser.repository.EventUserRepository;
import com.hyundai.softeer.backend.domain.subevent.dto.SubEventRequest;
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

    @Transactional
    public SharedUrlDto getSharedUrl(User user, SubEventRequest subEventRequest) {

        EventUser eventUser = eventUserRepository.findByUserIdAndSubEventId(user.getId(), subEventRequest.getSubEventId())
                .orElseThrow(() -> new EventUserNotFoundException());

        String sharedUrl = eventUser.getSharedUrl();
        if (sharedUrl.isBlank()) {
            sharedUrl = RandomStringUtils.random(SHARED_URL_LENGTH, true, true);
            eventUser.updateSharedUrl(sharedUrl);
            eventUserRepository.save(eventUser);
        }

        double gameScore = eventUser.getGameScore();

        if (gameScore <= 0.0) {
            throw new NonPlayedUserException();
        }

        return new SharedUrlDto(sharedUrl, gameScore);
    }

    public RedirectUrlDto getRedirectUrl(SharedUrlRequest sharedUrlRequest) {
        EventUser eventUser = eventUserRepository.findBySharedUrl(sharedUrlRequest.getSharedUrl())
                .orElseThrow(() -> new SharedUrlNotFoundException());

        eventUser.scoreSharedScore();
        eventUserRepository.save(eventUser);

        return new RedirectUrlDto(eventUser.getSubEvent().getEventType().getRedirectUrl());
    }
}
