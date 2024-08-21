package com.hyundai.softeer.backend.domain.eventuser.service;

import com.hyundai.softeer.backend.domain.eventuser.dto.*;
import com.hyundai.softeer.backend.domain.eventuser.entity.EventUser;
import com.hyundai.softeer.backend.domain.eventuser.exception.EventUserNotFoundException;
import com.hyundai.softeer.backend.domain.eventuser.exception.EventUserPageNotFoundException;
import com.hyundai.softeer.backend.domain.eventuser.exception.NonPlayedUserException;
import com.hyundai.softeer.backend.domain.eventuser.exception.SharedUrlNotFoundException;
import com.hyundai.softeer.backend.domain.eventuser.projection.EventUserPageProjection;
import com.hyundai.softeer.backend.domain.eventuser.repository.EventUserRepository;
import com.hyundai.softeer.backend.domain.subevent.dto.SubEventRequest;
import com.hyundai.softeer.backend.domain.subevent.exception.SubEventNotFoundException;
import com.hyundai.softeer.backend.domain.subevent.repository.SubEventRepository;
import com.hyundai.softeer.backend.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventUserService {
    public static final int SHARED_URL_LENGTH = 6;
    public static final int PAGE_SIZE = 15;
    private final EventUserRepository eventUserRepository;
    private final SubEventRepository subEventRepository;

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

        double gameScore = eventUser.getGameScore();

        if (gameScore <= 0.0) {
            throw new NonPlayedUserException();
        }

        String sharedUrl = eventUser.getSharedUrl();
        if (sharedUrl == null || sharedUrl.isEmpty()) {
            sharedUrl = RandomStringUtils.random(SHARED_URL_LENGTH, true, true);
            eventUser.updateSharedUrl(sharedUrl);
            eventUserRepository.save(eventUser);
        }


        return new SharedUrlDto(sharedUrl, gameScore);
    }

    @Transactional(readOnly = true)
    public EventUserPageResponseDto getUserPage(EventUserPageRequest eventUserPageRequest) {
        Long subEventId = eventUserPageRequest.getSubEventId();
        Integer pageSequence = eventUserPageRequest.getPageSequence();

        Pageable pageable = PageRequest.of(
                pageSequence,
                PAGE_SIZE
        );

        if (!subEventRepository.existsById(subEventId)) {
            throw new SubEventNotFoundException();
        }

        Page<EventUserPageProjection> eventUsersWithWinnerStatus = eventUserRepository.findEventUsersWithWinnerStatus(subEventId, pageable);

        if (eventUsersWithWinnerStatus.isEmpty()) {
            throw new EventUserPageNotFoundException();
        }

        int totalPages = eventUsersWithWinnerStatus.getTotalPages();
        List<UserInfoDtoWithIsWinner> users = eventUsersWithWinnerStatus.get()
                .map(eventUserPageProjection -> {
                    User user = eventUserPageProjection.getUser();
                    return UserInfoDtoWithIsWinner.builder()
                            .userId(user.getId())
                            .name(user.getName())
                            .email(user.getEmail())
                            .isWinner(eventUserPageProjection.getIsWinner())
                            .phoneNumber(user.getPhoneNumber())
                            .birthDate(user.getBirthDate())
                            .build();
                })
                .toList();

        return EventUserPageResponseDto.builder()
                .totalPages(totalPages)
                .users(users)
                .build();
    }

    public RedirectUrlDto getRedirectUrl(String sharedUrl) {
        EventUser eventUser = eventUserRepository.findBySharedUrl(sharedUrl)
                .orElseThrow(() -> new SharedUrlNotFoundException());

        eventUser.scoreSharedScore();
        eventUserRepository.save(eventUser);

        return new RedirectUrlDto(eventUser.getSubEvent().getEventType().getRedirectUrl());
    }
}
