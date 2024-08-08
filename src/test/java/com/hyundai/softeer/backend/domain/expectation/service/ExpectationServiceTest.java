package com.hyundai.softeer.backend.domain.expectation.service;

import com.hyundai.softeer.backend.domain.event.entity.Event;
import com.hyundai.softeer.backend.domain.event.exception.EventNotFoundException;
import com.hyundai.softeer.backend.domain.event.repository.EventRepository;
import com.hyundai.softeer.backend.domain.expectation.dto.*;
import com.hyundai.softeer.backend.domain.expectation.entity.Expectation;
import com.hyundai.softeer.backend.domain.expectation.exception.ExpectationNotFoundException;
import com.hyundai.softeer.backend.domain.expectation.repository.ExpectationRepository;
import com.hyundai.softeer.backend.domain.user.entity.User;
import com.hyundai.softeer.backend.global.jwt.OAuthProvider;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;

@Slf4j
class ExpectationServiceTest {

    @InjectMocks
    ExpectationService expectationService;

    @Mock
    ExpectationRepository expectationRepository;

    @Mock
    EventRepository eventRepository;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    @DisplayName("기대평 api: 정상 반환인 경우")
    void expectationTest() {
        // given
        int pageNumber= 0;
        ExpectationPageResponseDto expectationPageResponseDto = new ExpectationPageResponseDto(
                "www.naver.com"
        );

        Event event = new Event();
        event.setExpectationBannerImgUrl("www.eBanner.com");
        ExpectationPageRequest expectationPageRequest = new ExpectationPageRequest(1L);
        when(eventRepository.findById(any(Long.class))).thenReturn(Optional.of(event));

        // when
        ExpectationPageResponseDto expectationPage = expectationService.getExpectationPage(expectationPageRequest);

        // then
        assertThat(expectationPage.getExpectationBannerImgUrl()).isEqualTo(event.getExpectationBannerImgUrl());
    }

    @Test
    @DisplayName("기대평 api: 이벤트가 존재하지 않는 경우")
    void expectationNotExistEventTest() {
        // given
        int pageNumber= 0;
        ExpectationPageResponseDto expectationPageResponseDto = new ExpectationPageResponseDto(
                "www.naver.com"
        );

        when(eventRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        ExpectationPageRequest expectationPageRequest = new ExpectationPageRequest(100L);

        // when
        Assertions.assertThatThrownBy(() -> {
            expectationService.getExpectationPage(expectationPageRequest);
        }).isInstanceOf(EventNotFoundException.class);
    }

    @Test
    @DisplayName("기대평 페이지네이션 api: 정상 반환")
    void expectationPagenationTest() {
        // given

        List<ExpectationContentDto> expectationContentDtos = makeMockExpectationContent();
        List<Expectation> expectations = makeMockExpectation();
        ExpectationsResponseDto expectationsResponseDto = new ExpectationsResponseDto(3, expectationContentDtos);

        Pageable pageable = PageRequest.of(
                1,
                11,
                Sort.by("createdAt").ascending()
        );

        Page<Expectation> pages = new PageImpl<>(expectations, pageable,22);

        when(expectationRepository.findByEventId(any(Long.class), any(Pageable.class))).thenReturn(pages);
        ExpectationsRequest expectationsRequest = new ExpectationsRequest(1, 1L);

        // when
        ExpectationsResponseDto expectationsDto = expectationService.getExpectations(expectationsRequest);

        // then
        assertThat(expectationsDto.getTotalPages()).isEqualTo(2);
        assertThat(expectationsDto.getExpectationContents()).containsExactlyElementsOf(expectationContentDtos);
    }

    @Test
    @DisplayName("기대평 페이지네이션 api: 기대평이 존재하지 않는 경우")
    void expectationPagenationNotExistTest() {
        // given

        Pageable pageable = PageRequest.of(
                0,
                11,
                Sort.by("createdAt").descending()
        );

        Page<Expectation> pages = new PageImpl<>(new ArrayList<>(), pageable, 0);

        when(expectationRepository.findByEventId(any(Long.class), any(Pageable.class))).thenReturn(pages);
        ExpectationsRequest expectationsRequest = new ExpectationsRequest(1, 1L);

        // when
        Assertions.assertThatThrownBy(() -> {
            expectationService.getExpectations(expectationsRequest);
        }).isInstanceOf(ExpectationNotFoundException.class);
    }

    @Test
    @DisplayName("기대평 제출 api: 기대평 제출을 위한 api")
    void expectationSubmitTest() {
        // Arrange
        long eventId = 1L;
        String comment = "Test comment";
        ExpectationRegisterRequest request = new ExpectationRegisterRequest(eventId, comment);
        User authenticatedUser = new User(
                "minjun@naver.com",
                "김민준",
                "010-6860-6823",
                LocalDate.now(),
                OAuthProvider.NAVER
        );

        Event mockEvent = Event.builder().build();
        mockEvent.setId(1L);
        mockEvent.setEventName("산타페 이벤트");

        when(eventRepository.getReferenceById(eventId)).thenReturn(mockEvent);
        when(expectationRepository.save(any(Expectation.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Expectation result = expectationService.expectationRegisterApi(request, authenticatedUser);
        Event resultEvent = result.getEvent();
        User resultUser = result.getUser();

        // Assert
        assertThat(result).isNotNull();

        assertThat(resultEvent.getEventName()).isEqualTo("산타페 이벤트");
        assertThat(resultEvent.getId()).isEqualTo(1L);

        assertThat(resultUser.getName()).isEqualTo("김민준");
        assertThat(resultUser.getEmail()).isEqualTo("minjun@naver.com");
        assertThat(resultUser.getOAuthProvider()).isEqualTo(OAuthProvider.NAVER);

        verify(eventRepository).getReferenceById(eventId);
        verify(expectationRepository).save(any(Expectation.class));
    }

    private List<ExpectationContentDto> makeMockExpectationContent() {
        List<ExpectationContentDto> result = new ArrayList<>();

        for(int i = 1; i <= 11; i++) {
           result.add(new ExpectationContentDto("김민준" + i, "안녕하세요" + i));
        }
        return result;
    }

    private List<Expectation> makeMockExpectation() {
        List<Expectation> expectations = new ArrayList<>();

        for(int i = 1; i <= 11; i++) {
            Expectation expectation = new Expectation();
            expectation.setExpectationComment("안녕하세요" + i);
            expectation.setCreatedAt(LocalDateTime.now().plusDays(i));
            User user = new User("a", "김민준" + i, "s", LocalDate.now() ,OAuthProvider.KAKAO);
            expectation.setUser(user);
            expectations.add(expectation);
        }

        return expectations;
    }
}