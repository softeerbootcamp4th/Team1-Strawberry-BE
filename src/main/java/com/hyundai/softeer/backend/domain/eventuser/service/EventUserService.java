package com.hyundai.softeer.backend.domain.eventuser.service;

import com.hyundai.softeer.backend.domain.eventuser.dto.*;
import com.hyundai.softeer.backend.domain.eventuser.entity.EventUser;
import com.hyundai.softeer.backend.domain.eventuser.exception.EventUserNotFoundException;
import com.hyundai.softeer.backend.domain.eventuser.exception.EventUserPageNotExistException;
import com.hyundai.softeer.backend.domain.eventuser.exception.NonPlayedUserException;
import com.hyundai.softeer.backend.domain.eventuser.exception.SharedUrlNotFoundException;
import com.hyundai.softeer.backend.domain.eventuser.projection.EventUserProjection;
import com.hyundai.softeer.backend.domain.eventuser.repository.EventUserRepository;
import com.hyundai.softeer.backend.domain.subevent.dto.SubEventRequest;
import com.hyundai.softeer.backend.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventUserService {
    public static final int SHARED_URL_LENGTH = 6;
    public static final int EVENT_USER_PAGE_SIZE = 15;
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

    @Transactional(readOnly = true)
    public EventUserPageResponseDto getEventUserPage(EventUserPageRequest eventUserPageRequest) {
        Long eventId = eventUserPageRequest.getEventId();

        Pageable pageable = PageRequest.of(
                eventUserPageRequest.getPageSequence(),
                EVENT_USER_PAGE_SIZE,
                Sort.by("id").descending()
        );

        List<EventUserProjection> eventUserWithWinnerState = eventUserRepository.findEventUserWithWinnerState(
                eventId,
                EVENT_USER_PAGE_SIZE * eventUserPageRequest.getPageSequence(),
                EVENT_USER_PAGE_SIZE
        );

        if(eventUserWithWinnerState.isEmpty()) {
           throw new EventUserPageNotExistException();
        }

        Integer totalUser = eventUserRepository.countEventUserSize(eventId);

        Integer totalPages = (totalUser + EVENT_USER_PAGE_SIZE - 1) / EVENT_USER_PAGE_SIZE;

        List<EventUserInfos> eventUserInfos = eventUserWithWinnerState.stream()
                .map(eventUserProjection -> {
                    return EventUserInfos.builder()
                            .name(eventUserProjection.getName())
                            .phoneNumber(eventUserProjection.getPhoneNumber())
                            .birthDate(eventUserProjection.getBirthDate())
                            .email(eventUserProjection.getEmail())
                            .isWinner(eventUserProjection.getIsWinner())
                            .build();
                }).toList();

        return EventUserPageResponseDto.builder()
                .totalPages(totalPages)
                .eventUserInfos(eventUserInfos)
                .build();
    }
}
