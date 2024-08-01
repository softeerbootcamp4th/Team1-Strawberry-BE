package com.hyundai.softeer.backend.domain.lottery.drawing.service;

import com.hyundai.softeer.backend.domain.eventuser.entity.EventUser;
import com.hyundai.softeer.backend.domain.eventuser.repository.EventUserRepository;
import com.hyundai.softeer.backend.domain.lottery.drawing.exception.DrawingNotFoundException;
import com.hyundai.softeer.backend.domain.subevent.entity.SubEvent;
import com.hyundai.softeer.backend.domain.subevent.enums.SubEventType;
import com.hyundai.softeer.backend.domain.subevent.repository.SubEventRepository;
import com.hyundai.softeer.backend.domain.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class DrawingLotteryServiceTest {

    @InjectMocks
    private DrawingLotteryService drawingLotteryService;

    @Mock
    private SubEventRepository subEventRepository;

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
        User user = new User();
        Long eventId = 1L;
        SubEvent drawingEvent = new SubEvent();
        drawingEvent.setEventType(SubEventType.DRAWING);
        EventUser eventUser = new EventUser();

        List<SubEvent> events = Arrays.asList(drawingEvent);

        // When
        when(subEventRepository.findByEventId(eventId)).thenReturn(events);
        when(eventUserRepository.findByUserIdAndSubEventId(user.getId(), drawingEvent.getId())).thenReturn(eventUser);

        // Then
        assertDoesNotThrow(() -> drawingLotteryService.getDrawingLotteryLand(user, eventId));
    }

    @Test
    @DisplayName("드로잉 추첨 이벤트 랜딩 페이지 조회 실패")
    void getDrawingLotteryLand_noDrawingEvent() {
        // Given
        User user = new User();
        Long eventId = 1L;
        SubEvent nonDrawingEvent = new SubEvent();
        nonDrawingEvent.setEventType(SubEventType.QUIZ);
        List<SubEvent> events = Arrays.asList(nonDrawingEvent);

        // When
        when(subEventRepository.findByEventId(eventId)).thenReturn(events);

        // Then
        assertThrows(DrawingNotFoundException.class, () -> drawingLotteryService.getDrawingLotteryLand(user, eventId));
    }
}