package com.hyundai.softeer.backend.domain.lottery.drawing.service;

import com.hyundai.softeer.backend.domain.eventuser.entity.EventUser;
import com.hyundai.softeer.backend.domain.eventuser.repository.EventUserRepository;
import com.hyundai.softeer.backend.domain.lottery.drawing.entity.DrawingLotteryEvent;
import com.hyundai.softeer.backend.domain.lottery.drawing.exception.DrawingNotFoundException;
import com.hyundai.softeer.backend.domain.lottery.drawing.repository.DrawingLotteryRepository;
import com.hyundai.softeer.backend.domain.lottery.exception.AlreadyDrawedException;
import com.hyundai.softeer.backend.domain.lottery.service.LotteryService;
import com.hyundai.softeer.backend.domain.prize.entity.Prize;
import com.hyundai.softeer.backend.domain.prize.repository.PrizeRepository;
import com.hyundai.softeer.backend.domain.subevent.dto.LotteryScoreWeight;
import com.hyundai.softeer.backend.domain.subevent.dto.WinnerCandidate;
import com.hyundai.softeer.backend.domain.subevent.dto.WinnerInfo;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
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
    @DisplayName("드로잉 추첨 이벤트 Not Found")
    void getDrawWinnerInDrawingEvent_notFound() {
        // Given
        Long subEventId = 1L;

        // When
        when(drawingLotteryRepository.findById(subEventId)).thenReturn(Optional.empty());

        // Then
        assertThrows(DrawingNotFoundException.class, () -> drawingLotteryService.drawWinner(subEventId));
    }

    @Test
    @DisplayName("드로잉 추첨 이벤트 AlreadyDraw")
    void getDrawWinnerInDrawingEvent_AlreadyDraw() {
        // Given
        Long subEventId = 1L;

        // When
        when(drawingLotteryRepository.findById(subEventId)).thenReturn(Optional.of(new DrawingLotteryEvent()));
        when(winnerRepository.findBySubEventId(subEventId)).thenReturn(Optional.of(List.of(new Winner())));

        // Then
        assertThrows(AlreadyDrawedException.class, () -> drawingLotteryService.drawWinner(subEventId));
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

        DrawingLotteryEvent drawingLotteryEvent = DrawingLotteryEvent.builder()
                .winnersMeta(winnersInfo)
                .build();
        Pageable pageable = PageRequest.of(0, 6);

        // When
        when(drawingLotteryRepository.findById(subEventId)).thenReturn(Optional.of(drawingLotteryEvent));
        when(winnerRepository.findBySubEventId(subEventId)).thenReturn(Optional.empty());
        when(eventUserRepository.countBySubEventId(subEventId)).thenReturn(10L);
        when(eventUserRepository.findNByRand(anyLong(), anyInt(), anyInt())).thenReturn(users);
        when(userRepository.getReferenceById(anyLong())).thenAnswer(invocation -> (
                User.builder().id(invocation.getArgument(0)).build()
        ));
        when(subEventRepository.getReferenceById(anyLong())).thenAnswer(invocation -> (
                SubEvent.builder().id(invocation.getArgument(0)).build()
        ));
        when(prizeRepository.getReferenceById(anyLong())).thenAnswer(invocation -> (
                Prize.builder().id(invocation.getArgument(0)).build()
        ));
        when(winnerRepository.saveAll(any())).thenReturn(null);

        List<WinnerCandidate> result = drawingLotteryService.drawWinner(subEventId);

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getUserId()).isEqualTo(1L);
        assertThat(result.get(1).getUserId()).isEqualTo(2L);
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

        DrawingLotteryEvent drawingLotteryEvent = DrawingLotteryEvent.builder()
                .winnersMeta(winnersInfo)
                .build();

        // When
        when(drawingLotteryRepository.findById(subEventId)).thenReturn(Optional.of(drawingLotteryEvent));
        when(winnerRepository.findBySubEventId(subEventId)).thenReturn(Optional.empty());
        when(eventUserRepository.countBySubEventId(subEventId)).thenReturn(10L);
        when(eventUserRepository.findNByRand(anyLong(), anyInt(), anyInt())).thenReturn(users);
        when(eventUserRepository.findRestByRand(anyLong(), anyInt())).thenReturn(extraUser);
        when(lotteryService.getWinners(any(), any(), anyInt())).thenReturn(winnerCandidates);
        when(userRepository.getReferenceById(anyLong())).thenAnswer(invocation -> (
                User.builder().id(invocation.getArgument(0)).build()
        ));
        when(subEventRepository.getReferenceById(anyLong())).thenAnswer(invocation -> (
                SubEvent.builder().id(invocation.getArgument(0)).build()
        ));
        when(prizeRepository.getReferenceById(anyLong())).thenAnswer(invocation -> (
                Prize.builder().id(invocation.getArgument(0)).build()
        ));
        when(winnerRepository.saveAll(any())).thenReturn(null);

        List<WinnerCandidate> result = drawingLotteryService.drawWinner(subEventId);

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getUserId()).isEqualTo(1L);
        assertThat(result.get(1).getUserId()).isEqualTo(2L);

        log.info("result: {}", result);

    }
}


