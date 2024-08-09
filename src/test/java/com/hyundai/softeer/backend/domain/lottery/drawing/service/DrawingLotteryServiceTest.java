package com.hyundai.softeer.backend.domain.lottery.drawing.service;

import com.hyundai.softeer.backend.domain.eventuser.entity.EventUser;
import com.hyundai.softeer.backend.domain.eventuser.repository.EventUserRepository;
import com.hyundai.softeer.backend.domain.lottery.drawing.dto.DrawingInfoDtos;
import com.hyundai.softeer.backend.domain.lottery.drawing.entity.DrawingLotteryEvent;
import com.hyundai.softeer.backend.domain.lottery.drawing.exception.DrawingNotFoundException;
import com.hyundai.softeer.backend.domain.lottery.drawing.repository.DrawingLotteryRepository;
import com.hyundai.softeer.backend.domain.subevent.dto.LotteryScoreWeight;
import com.hyundai.softeer.backend.domain.subevent.dto.WinnerCandidate;
import com.hyundai.softeer.backend.domain.subevent.dto.WinnerInfo;
import com.hyundai.softeer.backend.domain.subevent.entity.SubEvent;
import com.hyundai.softeer.backend.domain.subevent.enums.SubEventType;
import com.hyundai.softeer.backend.domain.subevent.repository.SubEventRepository;
import com.hyundai.softeer.backend.domain.user.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Slf4j
class DrawingLotteryServiceTest {

    @InjectMocks
    private DrawingLotteryService drawingLotteryService;

    @Mock
    private SubEventRepository subEventRepository;

    @Mock
    private DrawingLotteryRepository drawingLotteryRepository;

    @Mock
    private EventUserRepository eventUserRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("드로잉 추첨 이벤트 랜딩 페이지 조회 성공")
    void getDrawingLotteryLand() {
        // Given
        User user = User.builder().id(1L).build();
        Long eventId = 1L;
        SubEvent drawingEvent = SubEvent.builder().id(1L).build();
        drawingEvent.setEventType(SubEventType.DRAWING);
        EventUser eventUser = new EventUser();

        List<SubEvent> events = Arrays.asList(drawingEvent);

        // When
        when(subEventRepository.findByEventId(eventId)).thenReturn(events);
        when(eventUserRepository.findByUserIdAndSubEventId(user.getId(), drawingEvent.getId())).thenReturn(Optional.of(eventUser));

        // Then
        assertDoesNotThrow(() -> drawingLotteryService.getDrawingLotteryLand(eventId));
    }

    @Test
    @DisplayName("드로잉 추첨 이벤트 랜딩 페이지 조회 실패")
    void getDrawingLotteryLand_noDrawingEvent() {
        // Given
        Long eventId = 1L;
        SubEvent nonDrawingEvent = new SubEvent();
        nonDrawingEvent.setEventType(SubEventType.QUIZ);
        List<SubEvent> events = Arrays.asList(nonDrawingEvent);

        // When
        when(subEventRepository.findByEventId(eventId)).thenReturn(events);

        // Then
        assertThrows(DrawingNotFoundException.class, () -> drawingLotteryService.getDrawingLotteryLand(eventId));
    }

    @Test
    @DisplayName("드로잉 추첨 이벤트 랜딩 페이지 조회 실패")
    void getWinners() {
        // Given
        List<EventUser> users = List.of(
                EventUser.builder().id(1L).user(User.builder().id(1L).build()).gameScore(50.0).lottoScore(1.0).priorityScore(1.0).sharedScore(1.0).build(),
                EventUser.builder().id(2L).user(User.builder().id(2L).build()).gameScore(30.0).lottoScore(1.0).priorityScore(1.0).sharedScore(1.0).build(),
                EventUser.builder().id(3L).user(User.builder().id(3L).build()).gameScore(1.0).lottoScore(1.0).priorityScore(1.0).sharedScore(1.0).build(),
                EventUser.builder().id(4L).user(User.builder().id(4L).build()).gameScore(1.0).lottoScore(1.0).priorityScore(1.0).sharedScore(1.0).build(),
                EventUser.builder().id(5L).user(User.builder().id(5L).build()).gameScore(1.0).lottoScore(1.0).priorityScore(1.0).sharedScore(1.0).build(),
                EventUser.builder().id(6L).user(User.builder().id(6L).build()).gameScore(1.0).lottoScore(1.0).priorityScore(1.0).sharedScore(1.0).build());

        Map<Integer, WinnerInfo> winnersMeta = Map.of(
                1, new WinnerInfo(2, 1L, 1));
        LotteryScoreWeight scoreWeight = new LotteryScoreWeight(1.0, 1.0, 1.0, 1.0);

        // When
        List<WinnerCandidate> winners = drawingLotteryService.getWinners(users, scoreWeight, 2);

        // Then
        assertThat(winners).hasSize(2);
        assertThat(winners.get(0).getUserId()).isEqualTo(1L);
        assertThat(winners.get(1).getUserId()).isEqualTo(2L);
    }

    @Test
    @DisplayName("드로잉 이벤트 게임 정보 조회 시 드로잉 이벤트가 없는 경우")
    void getDrawingGameInfo_DrawingNotFound() {
        // Given
        Long subEventId = 1L;
        List<DrawingLotteryEvent> drawingEvents = List.of();

        // When
        when(drawingLotteryRepository.findBySubEventId(any())).thenReturn(drawingEvents);

        // Then
        assertThrows(DrawingNotFoundException.class, () -> drawingLotteryService.getDrawingGameInfo(subEventId));
    }

    @Test
    @DisplayName("드로잉 이벤트 게임 정보 조회 성공")
    void getDrawingGameInfo_success() {
        // Given
        Long subEventId = 1L;
        List<DrawingLotteryEvent> drawingEvents = List.of(
                DrawingLotteryEvent.builder().id(1L).sequence(1).build(),
                DrawingLotteryEvent.builder().id(2L).sequence(2).build(),
                DrawingLotteryEvent.builder().id(3L).sequence(3).build()
        );

        // When
        when(drawingLotteryRepository.findBySubEventId(any())).thenReturn(drawingEvents);

        DrawingInfoDtos drawingGameInfos = drawingLotteryService.getDrawingGameInfo(subEventId);

        // Then
        assertThat(drawingGameInfos.getGameInfos()).hasSize(3);
        assertThat(drawingGameInfos.getGameInfos().get(0).getSequence()).isEqualTo(1);
    }
}


