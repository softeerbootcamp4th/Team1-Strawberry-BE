package com.hyundai.softeer.backend.domain.subevent.service;

import com.hyundai.softeer.backend.domain.eventuser.entity.EventUser;
import com.hyundai.softeer.backend.domain.eventuser.repository.EventUserRepository;
import com.hyundai.softeer.backend.domain.lottery.drawing.exception.DrawingNotFoundException;
import com.hyundai.softeer.backend.domain.lottery.drawing.service.DrawingLotteryService;
import com.hyundai.softeer.backend.domain.lottery.exception.AlreadyDrawedException;
import com.hyundai.softeer.backend.domain.lottery.service.LotteryService;
import com.hyundai.softeer.backend.domain.prize.entity.Prize;
import com.hyundai.softeer.backend.domain.prize.repository.PrizeRepository;
import com.hyundai.softeer.backend.domain.subevent.dto.WinnerCandidate;
import com.hyundai.softeer.backend.domain.subevent.entity.SubEvent;
import com.hyundai.softeer.backend.domain.subevent.enums.SubEventType;
import com.hyundai.softeer.backend.domain.subevent.repository.SubEventRepository;
import com.hyundai.softeer.backend.domain.user.entity.User;
import com.hyundai.softeer.backend.domain.user.repository.UserRepository;
import com.hyundai.softeer.backend.domain.winner.entity.Winner;
import com.hyundai.softeer.backend.domain.winner.repository.WinnerRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@Slf4j
class SubEventServiceTest {
    @InjectMocks
    private SubEventService subEventService;

    @Mock
    private SubEventRepository subEventRepository;

    @Mock
    private DrawingLotteryService drawingLotteryService;

    @Mock
    private EventUserRepository eventUserRepository;

    @Mock
    private WinnerRepository winnerRepository;

    @Mock
    private LotteryService lotteryService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PrizeRepository prizeRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    @DisplayName("드로잉 추첨 이벤트 Not Found")
    void getDrawWinnerInDrawingEvent_notFound() {
        // Given
        Long subEventId = 1L;

        // When
        when(subEventRepository.findById(subEventId)).thenReturn(Optional.empty());

        // Then
        assertThrows(DrawingNotFoundException.class, () -> subEventService.drawWinner(subEventId));
    }

    @Test
    @DisplayName("드로잉 추첨 이벤트 is Not Drawing")
    void getDrawWinnerInDrawingEvent_notDrawing() {
        // Given
        Long subEventId = 1L;

        // When
        when(subEventRepository.findById(subEventId)).thenReturn(Optional.of(com.hyundai.softeer.backend.domain.subevent.entity.SubEvent.builder().eventType(SubEventType.QUIZ).build()));

        // Then
        assertThrows(DrawingNotFoundException.class, () -> subEventService.drawWinner(subEventId));
    }

    @Test
    @DisplayName("드로잉 추첨 이벤트 AlreadyDraw")
    void getDrawWinnerInDrawingEvent_AlreadyDraw() {
        // Given
        Long subEventId = 1L;

        // When
        when(subEventRepository.findById(subEventId)).thenReturn(Optional.of(SubEvent.builder().eventType(SubEventType.DRAWING).build()));
        when(winnerRepository.findBySubEventId(subEventId)).thenReturn(Optional.of(List.of(new Winner())));

        // Then
        assertThrows(AlreadyDrawedException.class, () -> subEventService.drawWinner(subEventId));
    }

    @Test
    @DisplayName("드로잉 추첨 이벤트 N 명 바로 추출")
    void getDrawWinnerInDrawingEvent() {
        // Given
        Long subEventId = 1L;
        Map<String, Object> winnersInfo = Map.of(
                "1", Map.of("winnerCount", 1, "prizeId", 1),
                "2", Map.of("winnerCount", 1, "prizeId", 2)
        );
        List<EventUser> users = List.of(
                EventUser.builder().id(1L).user(User.builder().id(1L).build()).gameScore(50.0).lottoScore(1.0).priorityScore(1.0).sharedScore(1.0).build(),
                EventUser.builder().id(2L).user(User.builder().id(2L).build()).gameScore(30.0).lottoScore(1.0).priorityScore(1.0).sharedScore(1.0).build(),
                EventUser.builder().id(3L).user(User.builder().id(3L).build()).gameScore(1.0).lottoScore(1.0).priorityScore(1.0).sharedScore(1.0).build(),
                EventUser.builder().id(4L).user(User.builder().id(4L).build()).gameScore(1.0).lottoScore(1.0).priorityScore(1.0).sharedScore(1.0).build(),
                EventUser.builder().id(5L).user(User.builder().id(5L).build()).gameScore(1.0).lottoScore(1.0).priorityScore(1.0).sharedScore(1.0).build(),
                EventUser.builder().id(6L).user(User.builder().id(6L).build()).gameScore(1.0).lottoScore(1.0).priorityScore(1.0).sharedScore(1.0).build());

        List<WinnerCandidate> winnerCandidates = List.of(
                WinnerCandidate.builder().userId(1L).randomValue(1).build(),
                WinnerCandidate.builder().userId(2L).randomValue(2).build(),
                WinnerCandidate.builder().userId(3L).randomValue(3).build(),
                WinnerCandidate.builder().userId(4L).randomValue(4).build(),
                WinnerCandidate.builder().userId(5L).randomValue(5).build(),
                WinnerCandidate.builder().userId(6L).randomValue(6).build()
        );

        SubEvent drawingLotteryEvent = SubEvent.builder()
                .eventType(SubEventType.DRAWING)
                .winnersMeta(winnersInfo)
                .build();

        // When
        when(subEventRepository.findById(subEventId)).thenReturn(Optional.of(drawingLotteryEvent));
        when(winnerRepository.findBySubEventId(subEventId)).thenReturn(Optional.empty());
        when(eventUserRepository.findMaxBySubEventId(subEventId)).thenReturn(10L);
        when(eventUserRepository.findNByRand(anyLong(), anyInt(), anyInt())).thenReturn(users);
        when(drawingLotteryService.getWinners(any(), any(), anyInt())).thenReturn(winnerCandidates);
        when(userRepository.getReferenceById(anyLong())).thenAnswer(invocation -> (
                User.builder().id(invocation.getArgument(0)).build()
        ));
        when(subEventRepository.getReferenceById(anyLong())).thenAnswer(invocation -> (
                com.hyundai.softeer.backend.domain.subevent.entity.SubEvent.builder().id(invocation.getArgument(0)).build()
        ));
        when(prizeRepository.getReferenceById(anyLong())).thenAnswer(invocation -> (
                Prize.builder().id(invocation.getArgument(0)).build()
        ));
        when(winnerRepository.saveAll(any())).thenReturn(null);

        List<WinnerCandidate> result = subEventService.drawWinner(subEventId);

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getUserId()).isEqualTo(6L);
        assertThat(result.get(1).getUserId()).isEqualTo(5L);
    }

    @Test
    @DisplayName("드로잉 추첨 이벤트 N 명 이하 추출 후 나머지 추출")
    void getDrawWinnerInDrawingEventwithExtra() {
        // Given
        Long subEventId = 1L;
        Map<String, Object> winnersInfo = Map.of(
                "1", Map.of("winnerCount", 1, "prizeId", 1),
                "2", Map.of("winnerCount", 1, "prizeId", 2)
        );
        List<EventUser> users = new ArrayList<>() {{
            add(EventUser.builder().id(1L).user(User.builder().id(1L).build()).gameScore(50.0).lottoScore(1.0).priorityScore(1.0).sharedScore(1.0).build());
            add(EventUser.builder().id(2L).user(User.builder().id(2L).build()).gameScore(30.0).lottoScore(1.0).priorityScore(1.0).sharedScore(1.0).build());
            add(EventUser.builder().id(3L).user(User.builder().id(3L).build()).gameScore(1.0).lottoScore(1.0).priorityScore(1.0).sharedScore(1.0).build());
            add(EventUser.builder().id(4L).user(User.builder().id(4L).build()).gameScore(1.0).lottoScore(1.0).priorityScore(1.0).sharedScore(1.0).build());
        }};

        List<EventUser> extraUser = List.of(
                EventUser.builder().id(5L).user(User.builder().id(5L).build()).gameScore(1.0).lottoScore(1.0).priorityScore(1.0).sharedScore(1.0).build(),
                EventUser.builder().id(6L).user(User.builder().id(6L).build()).gameScore(1.0).lottoScore(1.0).priorityScore(1.0).sharedScore(1.0).build());

        List<WinnerCandidate> winnerCandidates = List.of(
                WinnerCandidate.builder().userId(1L).randomValue(1).build(),
                WinnerCandidate.builder().userId(2L).randomValue(2).build(),
                WinnerCandidate.builder().userId(3L).randomValue(3).build(),
                WinnerCandidate.builder().userId(4L).randomValue(4).build(),
                WinnerCandidate.builder().userId(5L).randomValue(5).build(),
                WinnerCandidate.builder().userId(6L).randomValue(6).build()
        );

        SubEvent drawingLotteryEvent = SubEvent.builder()
                .eventType(SubEventType.DRAWING)
                .winnersMeta(winnersInfo)
                .build();

        // When
        when(subEventRepository.findById(subEventId)).thenReturn(Optional.of(drawingLotteryEvent));
        when(winnerRepository.findBySubEventId(subEventId)).thenReturn(Optional.empty());
        when(eventUserRepository.findMaxBySubEventId(subEventId)).thenReturn(10L);
        when(eventUserRepository.findNByRand(anyLong(), anyInt(), anyInt())).thenReturn(users);
        when(eventUserRepository.findRestByRand(anyLong(), anyInt())).thenReturn(extraUser);
        when(drawingLotteryService.getWinners(any(), any(), anyInt())).thenReturn(winnerCandidates);
        when(userRepository.getReferenceById(anyLong())).thenAnswer(invocation -> (
                User.builder().id(invocation.getArgument(0)).build()
        ));
        when(subEventRepository.getReferenceById(anyLong())).thenAnswer(invocation -> (
                com.hyundai.softeer.backend.domain.subevent.entity.SubEvent.builder().id(invocation.getArgument(0)).build()
        ));
        when(prizeRepository.getReferenceById(anyLong())).thenAnswer(invocation -> (
                Prize.builder().id(invocation.getArgument(0)).build()
        ));
        when(winnerRepository.saveAll(any())).thenReturn(null);

        List<WinnerCandidate> result = subEventService.drawWinner(subEventId);

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getUserId()).isEqualTo(6L);
        assertThat(result.get(1).getUserId()).isEqualTo(5L);

        log.info("result: {}", result);

    }
}


