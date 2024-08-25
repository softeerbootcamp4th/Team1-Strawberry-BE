package com.hyundai.softeer.backend.domain.expectation.service;

import com.hyundai.softeer.backend.domain.event.entity.Event;
import com.hyundai.softeer.backend.domain.event.exception.EventNotFoundException;
import com.hyundai.softeer.backend.domain.event.repository.EventRepository;
import com.hyundai.softeer.backend.domain.eventuser.entity.EventUser;
import com.hyundai.softeer.backend.domain.eventuser.repository.EventUserRepository;
import com.hyundai.softeer.backend.domain.expectation.constant.ExpectationPage;
import com.hyundai.softeer.backend.domain.expectation.dto.*;
import com.hyundai.softeer.backend.domain.expectation.entity.Expectation;
import com.hyundai.softeer.backend.domain.expectation.exception.ExpectationNotFoundException;
import com.hyundai.softeer.backend.domain.expectation.repository.ExpectationRepository;
import com.hyundai.softeer.backend.domain.subevent.entity.SubEvent;
import com.hyundai.softeer.backend.domain.subevent.enums.SubEventExecuteType;
import com.hyundai.softeer.backend.domain.subevent.repository.SubEventRepository;
import com.hyundai.softeer.backend.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExpectationService {

    private final ExpectationRepository expectationRepository;
    private final EventRepository eventRepository;
    private final SubEventRepository subEventRepository;
    private final EventUserRepository eventUserRepository;

    @Transactional(readOnly = true)
    public ExpectationPageResponseDto getExpectationPage(long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException());

        return new ExpectationPageResponseDto(event.getExpectationBannerImgUrl());
    }

    @Transactional(readOnly = true)
    public ExpectationsResponseDto getExpectations(ExpectationsRequest expectationsRequest, long eventId) {
        Sort sort = Sort.by("createdAt").descending();

        Pageable pageable = PageRequest.of(
                expectationsRequest.getPageSequence(),
                ExpectationPage.PAGE_SIZE,
                sort
        );

        Page<Expectation> expectationPage = expectationRepository.findByEventId(
                eventId,
                pageable
        );

        if (expectationPage == null) {
            throw new EventNotFoundException();
        }

        List<ExpectationContentDto> expectationContentDtos = expectationPage.get()
                .map((expectation -> {
                    String expectationComment = expectation.getExpectationComment();
                    String name = expectation.getUser().getName();
                    name = maskName(name);
                    return new ExpectationContentDto(name, expectationComment);
                }))
                .toList();

        if (expectationContentDtos.isEmpty()) {
            throw new ExpectationNotFoundException();
        }

        return new ExpectationsResponseDto(
                expectationPage.getTotalPages(),
                expectationContentDtos
        );
    }

    @Transactional
    public Expectation expectationRegisterApi(
            ExpectationRegisterRequest expectationRegisterRequest,
            long eventId,
            User authenticatedUser
    ) {
        Expectation expectation = new Expectation();

        expectation.setEvent(eventRepository.getReferenceById(eventId));
        expectation.setUser(authenticatedUser);
        expectation.setExpectationComment(expectationRegisterRequest.getComment());

        List<SubEvent> subEvents = subEventRepository.findByEventIdAndExecuteType(eventId, SubEventExecuteType.LOTTERY);
        LocalDateTime now = LocalDateTime.now();

        for (SubEvent subEvent : subEvents) {
            EventUser eventUser = eventUserRepository.findByUserIdAndSubEventId(authenticatedUser.getId(), subEvent.getId())
                    .orElseGet(() -> EventUser.builder()
                            .user(authenticatedUser)
                            .subEvent(subEventRepository.getReferenceById(subEvent.getId()))
                            .lastVisitedAt(now)
                            .lastChargeAt(now)
                            .build());
            eventUser.updateExpectationBonusChanceIfNotUsed();

            eventUserRepository.save(eventUser);
        }

        return expectationRepository.save(expectation);
    }

    public static String maskName(String name) {
        if (name == null || name.length() < 2) {
            return name;
        }

        return name.substring(0, 1) + "*" + name.substring(2);
    }
}
