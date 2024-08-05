package com.hyundai.softeer.backend.domain.expectation.service;

import com.hyundai.softeer.backend.domain.event.entity.Event;
import com.hyundai.softeer.backend.domain.event.exception.EventNotFoundException;
import com.hyundai.softeer.backend.domain.event.repository.EventRepository;
import com.hyundai.softeer.backend.domain.expectation.constant.ExpectationPage;
import com.hyundai.softeer.backend.domain.expectation.dto.*;
import com.hyundai.softeer.backend.domain.expectation.entity.Expectation;
import com.hyundai.softeer.backend.domain.expectation.exception.ExpectationNotFoundException;
import com.hyundai.softeer.backend.domain.expectation.repository.ExpectationRepository;
import com.hyundai.softeer.backend.global.dto.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExpectationService {

    private final ExpectationRepository expectationRepository;
    private final EventRepository eventRepository;

    @Transactional(readOnly = true)
    public ExpectationPageResponseDto getExpectationPage(ExpectationPageRequest expectationPageRequest) {
        Long eventId = expectationPageRequest.getEventId();

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException());

        return new ExpectationPageResponseDto(event.getExpectationBannerImgUrl());
    }

    @Transactional(readOnly = true)
    public ExpectationsResponseDto getExpectations(ExpectationsRequest expectationsRequest) {
        Sort sort = Sort.by("createdAt").ascending();

        Pageable pageable = PageRequest.of(
                expectationsRequest.getPageSequence(),
                ExpectationPage.PAGE_SIZE,
                sort
        );

        Page<Expectation> expectationPage = expectationRepository.findByEventId(
                expectationsRequest.getEventId(),
                pageable
        );

        if(expectationPage == null) {
           throw new EventNotFoundException();
        }

        List<ExpectationContentDto>  expectationContentDtos = expectationPage.get()
                .map((expectation -> {
                    String expectationComment = expectation.getExpectationComment();
                    String name = expectation.getUser().getName();
                    return new ExpectationContentDto(name, expectationComment);
                }))
                .toList();

        if(expectationContentDtos.isEmpty()) {
            throw new ExpectationNotFoundException();
        }

        return new ExpectationsResponseDto(
                expectationPage.getTotalPages(),
                expectationContentDtos
        );
    }
}
