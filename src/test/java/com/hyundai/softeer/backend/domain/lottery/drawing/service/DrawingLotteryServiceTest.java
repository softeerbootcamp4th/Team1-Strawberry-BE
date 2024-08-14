package com.hyundai.softeer.backend.domain.lottery.drawing.service;

import com.hyundai.softeer.backend.domain.eventuser.entity.EventUser;
import com.hyundai.softeer.backend.domain.eventuser.exception.NoChanceUserException;
import com.hyundai.softeer.backend.domain.eventuser.repository.EventUserRepository;
import com.hyundai.softeer.backend.domain.lottery.drawing.dto.*;
import com.hyundai.softeer.backend.domain.lottery.drawing.entity.DrawingLotteryEvent;
import com.hyundai.softeer.backend.domain.lottery.drawing.exception.DrawingNotFoundException;
import com.hyundai.softeer.backend.domain.lottery.drawing.repository.DrawingLotteryRepository;
import com.hyundai.softeer.backend.domain.subevent.dto.LotteryScoreWeight;
import com.hyundai.softeer.backend.domain.subevent.dto.WinnerCandidate;
import com.hyundai.softeer.backend.domain.subevent.dto.WinnerInfo;
import com.hyundai.softeer.backend.domain.subevent.entity.SubEvent;
import com.hyundai.softeer.backend.domain.subevent.enums.EventPlayType;
import com.hyundai.softeer.backend.domain.subevent.enums.SubEventType;
import com.hyundai.softeer.backend.domain.subevent.repository.SubEventRepository;
import com.hyundai.softeer.backend.domain.user.entity.User;
import com.hyundai.softeer.backend.global.utils.ParseUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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

    @Spy
    private ScoreCalculator scoreCalculator;

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
        DrawingInfoRequest drawingInfoRequest = new DrawingInfoRequest(1L, EventPlayType.NORMAL);
        List<DrawingLotteryEvent> drawingEvents = List.of();
        User authenticatedUser = User.builder().id(1L).build();

        // When
        when(drawingLotteryRepository.findBySubEventId(any())).thenReturn(drawingEvents);

        // Then
        assertThrows(DrawingNotFoundException.class, () -> drawingLotteryService.getDrawingGameInfo(authenticatedUser, drawingInfoRequest));
    }

    @Test
    @DisplayName("드로잉 이벤트 게임 정보 조회 성공")
    void getDrawingGameInfo_success() {
        // Given
        DrawingInfoRequest drawingInfoRequest = new DrawingInfoRequest(1L, EventPlayType.NORMAL);
        List<DrawingLotteryEvent> drawingEvents = List.of(
                DrawingLotteryEvent.builder().id(1L).sequence(1).startPosX(1.0).startPosY(1.5).build(),
                DrawingLotteryEvent.builder().id(2L).sequence(2).startPosX(2.0).startPosY(1.5).build(),
                DrawingLotteryEvent.builder().id(3L).sequence(3).startPosX(1.5).startPosY(4.0).build()
        );
        User authenticatedUser = User.builder().id(1L).build();
        SubEvent subEvent = SubEvent.builder().id(1L).build();
        EventUser eventUser = EventUser.builder()
                .user(authenticatedUser)
                .subEvent(subEvent)
                .chance(1)
                .lastVisitedAt(LocalDateTime.now())
                .lastChargeAt(LocalDateTime.now())
                .build();

        // When
        when(drawingLotteryRepository.findBySubEventId(any())).thenReturn(drawingEvents);
        when(eventUserRepository.findByUserIdAndSubEventId(anyLong(), anyLong())).thenReturn(Optional.of(eventUser));
        when(eventUserRepository.save(any())).thenReturn(null);

        DrawingInfoDtos drawingGameInfos = drawingLotteryService.getDrawingGameInfo(authenticatedUser, drawingInfoRequest);

        // Then
        assertThat(drawingGameInfos.getGameInfos()).hasSize(3);
        assertThat(drawingGameInfos.getGameInfos().get(0).getSequence()).isEqualTo(1);
        log.info("drawingGameInfos: {}", drawingGameInfos);
    }

    @Test
    @DisplayName("드로잉 이벤트 게임 정보 조회 시 유저 기회 0번인 경우")
    void getDrawingGameInfo_no_chance() {
        // Given
        DrawingInfoRequest drawingInfoRequest = new DrawingInfoRequest(1L, EventPlayType.NORMAL);
        List<DrawingLotteryEvent> drawingEvents = List.of(
                DrawingLotteryEvent.builder().id(1L).sequence(1).startPosX(1.0).startPosY(1.5).build(),
                DrawingLotteryEvent.builder().id(2L).sequence(2).startPosX(2.0).startPosY(1.5).build(),
                DrawingLotteryEvent.builder().id(3L).sequence(3).startPosX(1.5).startPosY(4.0).build()
        );
        User authenticatedUser = User.builder().id(1L).build();
        SubEvent subEvent = SubEvent.builder().id(1L).build();
        EventUser eventUser = EventUser.builder()
                .user(authenticatedUser)
                .subEvent(subEvent)
                .chance(0)
                .lastVisitedAt(LocalDateTime.now())
                .lastChargeAt(LocalDateTime.now())
                .build();

        // When
        when(drawingLotteryRepository.findBySubEventId(any())).thenReturn(drawingEvents);
        when(eventUserRepository.findByUserIdAndSubEventId(anyLong(), anyLong())).thenReturn(Optional.of(eventUser));

        // Then
        assertThrows(NoChanceUserException.class, () -> drawingLotteryService.getDrawingGameInfo(authenticatedUser, drawingInfoRequest));
    }

    @Test
    @DisplayName("드로잉 이벤트 게임 정보 조회 시 첫 참여 유저")
    void getDrawingGameInfo_first_playing() {
        // Given
        DrawingInfoRequest drawingInfoRequest = new DrawingInfoRequest(1L, EventPlayType.NORMAL);
        List<DrawingLotteryEvent> drawingEvents = List.of(
                DrawingLotteryEvent.builder().id(1L).sequence(1).startPosX(1.0).startPosY(1.5).build(),
                DrawingLotteryEvent.builder().id(2L).sequence(2).startPosX(2.0).startPosY(1.5).build(),
                DrawingLotteryEvent.builder().id(3L).sequence(3).startPosX(1.5).startPosY(4.0).build()
        );
        User authenticatedUser = User.builder().id(1L).build();

        // When
        when(drawingLotteryRepository.findBySubEventId(any())).thenReturn(drawingEvents);
        when(eventUserRepository.findByUserIdAndSubEventId(anyLong(), anyLong())).thenReturn(Optional.empty());
        when(eventUserRepository.save(any())).thenReturn(null);

        DrawingInfoDtos drawingGameInfo = drawingLotteryService.getDrawingGameInfo(authenticatedUser, drawingInfoRequest);

        // Then
        assertThat(drawingGameInfo.getChance()).isEqualTo(1);
    }

    @Test
    @DisplayName("드로잉 이벤트 게임 정보 조회 시 유저 기회 1번, 충전 시간 4시간 미만일 경우")
    void getDrawingGameInfo_1_chance_less_than_4hours_last_played() {
        // Given
        DrawingInfoRequest drawingInfoRequest = new DrawingInfoRequest(1L, EventPlayType.NORMAL);
        List<DrawingLotteryEvent> drawingEvents = List.of(
                DrawingLotteryEvent.builder().id(1L).sequence(1).startPosX(1.0).startPosY(1.5).build(),
                DrawingLotteryEvent.builder().id(2L).sequence(2).startPosX(2.0).startPosY(1.5).build(),
                DrawingLotteryEvent.builder().id(3L).sequence(3).startPosX(1.5).startPosY(4.0).build()
        );
        User authenticatedUser = User.builder().id(1L).build();
        SubEvent subEvent = SubEvent.builder().id(1L).build();
        EventUser eventUser = EventUser.builder()
                .user(authenticatedUser)
                .subEvent(subEvent)
                .chance(1)
                .lastVisitedAt(LocalDateTime.now())
                .lastChargeAt(LocalDateTime.now())
                .build();

        // When
        when(drawingLotteryRepository.findBySubEventId(any())).thenReturn(drawingEvents);
        when(eventUserRepository.findByUserIdAndSubEventId(anyLong(), anyLong())).thenReturn(Optional.of(eventUser));
        when(eventUserRepository.save(any())).thenReturn(null);

        DrawingInfoDtos drawingGameInfo = drawingLotteryService.getDrawingGameInfo(authenticatedUser, drawingInfoRequest);

        // Then
        assertThat(drawingGameInfo.getChance()).isEqualTo(0);
    }

    @Test
    @DisplayName("드로잉 이벤트 게임 정보 조회 시 유저 기회 0번, 충전 시간 4시간 이상일 경우")
    void getDrawingGameInfo_no_chance_but_4hours_last_played() {
        // Given
        DrawingInfoRequest drawingInfoRequest = new DrawingInfoRequest(1L, EventPlayType.NORMAL);
        List<DrawingLotteryEvent> drawingEvents = List.of(
                DrawingLotteryEvent.builder().id(1L).sequence(1).startPosX(1.0).startPosY(1.5).build(),
                DrawingLotteryEvent.builder().id(2L).sequence(2).startPosX(2.0).startPosY(1.5).build(),
                DrawingLotteryEvent.builder().id(3L).sequence(3).startPosX(1.5).startPosY(4.0).build()
        );
        User authenticatedUser = User.builder().id(1L).build();
        SubEvent subEvent = SubEvent.builder().id(1L).build();
        EventUser eventUser = EventUser.builder()
                .user(authenticatedUser)
                .subEvent(subEvent)
                .chance(0)
                .lastVisitedAt(LocalDateTime.now())
                .lastChargeAt(LocalDateTime.now().minusHours(4))
                .build();

        // When
        when(drawingLotteryRepository.findBySubEventId(any())).thenReturn(drawingEvents);
        when(eventUserRepository.findByUserIdAndSubEventId(anyLong(), anyLong())).thenReturn(Optional.of(eventUser));
        when(eventUserRepository.save(any())).thenReturn(null);

        DrawingInfoDtos drawingGameInfo = drawingLotteryService.getDrawingGameInfo(authenticatedUser, drawingInfoRequest);

        // Then
        assertThat(drawingGameInfo.getChance()).isEqualTo(0);
    }

    @Test
    @DisplayName("드로잉 이벤트 게임 정보 조회 시 유저 기회 1번, 충전 시간 4시간 이상일 경우")
    void getDrawingGameInfo_1_chance_and_4hours_last_played() {
        // Given
        DrawingInfoRequest drawingInfoRequest = new DrawingInfoRequest(1L, EventPlayType.NORMAL);
        List<DrawingLotteryEvent> drawingEvents = List.of(
                DrawingLotteryEvent.builder().id(1L).sequence(1).startPosX(1.0).startPosY(1.5).build(),
                DrawingLotteryEvent.builder().id(2L).sequence(2).startPosX(2.0).startPosY(1.5).build(),
                DrawingLotteryEvent.builder().id(3L).sequence(3).startPosX(1.5).startPosY(4.0).build()
        );
        User authenticatedUser = User.builder().id(1L).build();
        SubEvent subEvent = SubEvent.builder().id(1L).build();
        EventUser eventUser = EventUser.builder()
                .user(authenticatedUser)
                .subEvent(subEvent)
                .chance(1)
                .lastVisitedAt(LocalDateTime.now())
                .lastChargeAt(LocalDateTime.now().minusHours(5))
                .build();

        // When
        when(drawingLotteryRepository.findBySubEventId(any())).thenReturn(drawingEvents);
        when(eventUserRepository.findByUserIdAndSubEventId(anyLong(), anyLong())).thenReturn(Optional.of(eventUser));
        when(eventUserRepository.save(any())).thenReturn(null);

        DrawingInfoDtos drawingGameInfo = drawingLotteryService.getDrawingGameInfo(authenticatedUser, drawingInfoRequest);

        // Then
        assertThat(drawingGameInfo.getChance()).isEqualTo(1);
    }

    @Test
    @DisplayName("드로잉 이벤트 게임 정보 조회 시 유저 기회 0번, 충전 시간 8시간 이상일 경우")
    void getDrawingGameInfo_0_chance_and_8hours_last_played() {
        // Given
        DrawingInfoRequest drawingInfoRequest = new DrawingInfoRequest(1L, EventPlayType.NORMAL);
        List<DrawingLotteryEvent> drawingEvents = List.of(
                DrawingLotteryEvent.builder().id(1L).sequence(1).startPosX(1.0).startPosY(1.5).build(),
                DrawingLotteryEvent.builder().id(2L).sequence(2).startPosX(2.0).startPosY(1.5).build(),
                DrawingLotteryEvent.builder().id(3L).sequence(3).startPosX(1.5).startPosY(4.0).build()
        );
        User authenticatedUser = User.builder().id(1L).build();
        SubEvent subEvent = SubEvent.builder().id(1L).build();
        EventUser eventUser = EventUser.builder()
                .user(authenticatedUser)
                .subEvent(subEvent)
                .chance(0)
                .lastVisitedAt(LocalDateTime.now())
                .lastChargeAt(LocalDateTime.now().minusHours(12))
                .build();

        // When
        when(drawingLotteryRepository.findBySubEventId(any())).thenReturn(drawingEvents);
        when(eventUserRepository.findByUserIdAndSubEventId(anyLong(), anyLong())).thenReturn(Optional.of(eventUser));
        when(eventUserRepository.save(any())).thenReturn(null);

        DrawingInfoDtos drawingGameInfo = drawingLotteryService.getDrawingGameInfo(authenticatedUser, drawingInfoRequest);

        // Then
        assertThat(drawingGameInfo.getChance()).isEqualTo(1);
    }


    @Test
    @DisplayName("드로잉 이벤트 게임 정보 조회 시 유저 기회 1번, 충전 시간 8시간 이상일 경우")
    void getDrawingGameInfo_1_chance_and_8hours_last_played() {
        // Given
        DrawingInfoRequest drawingInfoRequest = new DrawingInfoRequest(1L, EventPlayType.NORMAL);
        List<DrawingLotteryEvent> drawingEvents = List.of(
                DrawingLotteryEvent.builder().id(1L).sequence(1).startPosX(1.0).startPosY(1.5).build(),
                DrawingLotteryEvent.builder().id(2L).sequence(2).startPosX(2.0).startPosY(1.5).build(),
                DrawingLotteryEvent.builder().id(3L).sequence(3).startPosX(1.5).startPosY(4.0).build()
        );
        User authenticatedUser = User.builder().id(1L).build();
        SubEvent subEvent = SubEvent.builder().id(1L).build();
        EventUser eventUser = EventUser.builder()
                .user(authenticatedUser)
                .subEvent(subEvent)
                .chance(1)
                .lastVisitedAt(LocalDateTime.now())
                .lastChargeAt(LocalDateTime.now().minusHours(12))
                .build();

        // When
        when(drawingLotteryRepository.findBySubEventId(any())).thenReturn(drawingEvents);
        when(eventUserRepository.findByUserIdAndSubEventId(anyLong(), anyLong())).thenReturn(Optional.of(eventUser));
        when(eventUserRepository.save(any())).thenReturn(null);

        DrawingInfoDtos drawingGameInfo = drawingLotteryService.getDrawingGameInfo(authenticatedUser, drawingInfoRequest);

        // Then
        assertThat(drawingGameInfo.getChance()).isEqualTo(1);
    }

    @ParameterizedTest
    @DisplayName("드로잉 이벤트 게임 정보 조회 시 기대평을 작성하지 않았거나 이미 사용한 유저의 경우")
    @ValueSource(ints = {0, -1})
    void getDrawingGameInfo_no_chance_expectation(int expectationBonusChance) {
        // Given
        DrawingInfoRequest drawingInfoRequest = new DrawingInfoRequest(1L, EventPlayType.EXPECTATION);
        List<DrawingLotteryEvent> drawingEvents = List.of(
                DrawingLotteryEvent.builder().id(1L).sequence(1).startPosX(1.0).startPosY(1.5).build(),
                DrawingLotteryEvent.builder().id(2L).sequence(2).startPosX(2.0).startPosY(1.5).build(),
                DrawingLotteryEvent.builder().id(3L).sequence(3).startPosX(1.5).startPosY(4.0).build()
        );
        User authenticatedUser = User.builder().id(1L).build();
        SubEvent subEvent = SubEvent.builder().id(1L).build();
        EventUser eventUser = EventUser.builder()
                .user(authenticatedUser)
                .subEvent(subEvent)
                .chance(0)
                .expectationBonusChance(expectationBonusChance)
                .lastVisitedAt(LocalDateTime.now())
                .lastChargeAt(LocalDateTime.now())
                .build();

        // When
        when(drawingLotteryRepository.findBySubEventId(any())).thenReturn(drawingEvents);
        when(eventUserRepository.findByUserIdAndSubEventId(anyLong(), anyLong())).thenReturn(Optional.of(eventUser));

        // Then
        assertThrows(NoChanceUserException.class, () -> drawingLotteryService.getDrawingGameInfo(authenticatedUser, drawingInfoRequest));
    }

    @Test
    @DisplayName("드로잉 이벤트 게임 정보 조회 시 기대평 작성 기회를 사용하는 경우")
    void getDrawingGameInfo_expectation_chance() {
        // Given
        DrawingInfoRequest drawingInfoRequest = new DrawingInfoRequest(1L, EventPlayType.EXPECTATION);
        List<DrawingLotteryEvent> drawingEvents = List.of(
                DrawingLotteryEvent.builder().id(1L).sequence(1).startPosX(1.0).startPosY(1.5).build(),
                DrawingLotteryEvent.builder().id(2L).sequence(2).startPosX(2.0).startPosY(1.5).build(),
                DrawingLotteryEvent.builder().id(3L).sequence(3).startPosX(1.5).startPosY(4.0).build()
        );

        User authenticatedUser = User.builder().id(1L).build();
        SubEvent subEvent = SubEvent.builder().id(1L).build();
        EventUser eventUser = EventUser.builder()
                .user(authenticatedUser)
                .subEvent(subEvent)
                .chance(1)
                .expectationBonusChance(1)
                .lastVisitedAt(LocalDateTime.now())
                .lastChargeAt(LocalDateTime.now())
                .build();

        // When
        when(drawingLotteryRepository.findBySubEventId(any())).thenReturn(drawingEvents);
        when(eventUserRepository.findByUserIdAndSubEventId(anyLong(), anyLong())).thenReturn(Optional.of(eventUser));
        when(eventUserRepository.save(any())).thenReturn(null);

        DrawingInfoDtos drawingGameInfo = drawingLotteryService.getDrawingGameInfo(authenticatedUser, drawingInfoRequest);

        // Then
        assertThat(drawingGameInfo.getChance()).isEqualTo(1);
    }

    @ParameterizedTest
    @DisplayName("드로잉 이벤트 게임 정보 조회 시 공유 URL 을 생성하지 않았거나 이미 사용한 유저의 경우")
    @ValueSource(ints = {0, -1})
    void getDrawingGameInfo_no_chance_shared(int sharedBonusChance) {
        // Given
        DrawingInfoRequest drawingInfoRequest = new DrawingInfoRequest(1L, EventPlayType.EXPECTATION);
        List<DrawingLotteryEvent> drawingEvents = List.of(
                DrawingLotteryEvent.builder().id(1L).sequence(1).startPosX(1.0).startPosY(1.5).build(),
                DrawingLotteryEvent.builder().id(2L).sequence(2).startPosX(2.0).startPosY(1.5).build(),
                DrawingLotteryEvent.builder().id(3L).sequence(3).startPosX(1.5).startPosY(4.0).build()
        );
        User authenticatedUser = User.builder().id(1L).build();
        SubEvent subEvent = SubEvent.builder().id(1L).build();
        EventUser eventUser = EventUser.builder()
                .user(authenticatedUser)
                .subEvent(subEvent)
                .chance(0)
                .shareBonusChance(sharedBonusChance)
                .lastVisitedAt(LocalDateTime.now())
                .lastChargeAt(LocalDateTime.now())
                .build();

        // When
        when(drawingLotteryRepository.findBySubEventId(any())).thenReturn(drawingEvents);
        when(eventUserRepository.findByUserIdAndSubEventId(anyLong(), anyLong())).thenReturn(Optional.of(eventUser));

        // Then
        assertThrows(NoChanceUserException.class, () -> drawingLotteryService.getDrawingGameInfo(authenticatedUser, drawingInfoRequest));
    }

    @Test
    @DisplayName("드로잉 이벤트 게임 정보 조회 시 공유 URL 작성 기회를 사용하는 경우")
    void getDrawingGameInfo_shared_url_chance() {
        // Given
        DrawingInfoRequest drawingInfoRequest = new DrawingInfoRequest(1L, EventPlayType.SHARED);
        List<DrawingLotteryEvent> drawingEvents = List.of(
                DrawingLotteryEvent.builder().id(1L).sequence(1).startPosX(1.0).startPosY(1.5).build(),
                DrawingLotteryEvent.builder().id(2L).sequence(2).startPosX(2.0).startPosY(1.5).build(),
                DrawingLotteryEvent.builder().id(3L).sequence(3).startPosX(1.5).startPosY(4.0).build()
        );

        User authenticatedUser = User.builder().id(1L).build();
        SubEvent subEvent = SubEvent.builder().id(1L).build();
        EventUser eventUser = EventUser.builder()
                .user(authenticatedUser)
                .subEvent(subEvent)
                .chance(1)
                .shareBonusChance(1)
                .lastVisitedAt(LocalDateTime.now())
                .lastChargeAt(LocalDateTime.now())
                .build();

        // When
        when(drawingLotteryRepository.findBySubEventId(any())).thenReturn(drawingEvents);
        when(eventUserRepository.findByUserIdAndSubEventId(anyLong(), anyLong())).thenReturn(Optional.of(eventUser));
        when(eventUserRepository.save(any())).thenReturn(null);

        DrawingInfoDtos drawingGameInfo = drawingLotteryService.getDrawingGameInfo(authenticatedUser, drawingInfoRequest);

        // Then
        assertThat(drawingGameInfo.getChance()).isEqualTo(1);
    }

    @Test
    @DisplayName("드로잉 이벤트 게임 점수 채점 시 드로잉 이벤트가 없는 경우")
    void getDrawingScore_DrawingNotFound() {
        // Given
        List<PositionDto> positions = List.of(new PositionDto(1.0, 1.5));
        DrawingScoreRequest drawingScoreRequest = DrawingScoreRequest.builder()
                .positions(positions)
                .subEventId(1L)
                .sequence(1)
                .build();

        User user = User.builder().id(1L).build();

        // When
        when(drawingLotteryRepository.findBySubEventIdAndSequence(any(), any())).thenReturn(Optional.empty());

        // Then
        assertThrows(DrawingNotFoundException.class, () -> drawingLotteryService.getDrawingScore(user, drawingScoreRequest));
    }

    @Test
    @DisplayName("드로잉 이벤트 게임 점수 채점 고득점")
    void getDrawingScore_success() {
        // Given
        List<PositionDto> positions = ParseUtil.parsePositionsFromJson("https://softeer-static.s3.ap-northeast-2.amazonaws.com/drawingLottery/answer/userPoints_example_01.json");

        DrawingScoreRequest drawingScoreRequest = DrawingScoreRequest.builder()
                .positions(positions)
                .subEventId(1L)
                .sequence(1)
                .build();

        DrawingLotteryEvent drawingEvent = DrawingLotteryEvent.builder().id(1L).sequence(1).startPosX(1.0).startPosY(1.5).drawPointsJsonUrl("https://softeer-static.s3.ap-northeast-2.amazonaws.com/drawingLottery/answer/contour_points_01.json").build();

        User user = User.builder().id(1L).build();
        EventUser eventUser = EventUser.builder().user(user).scores(new HashMap<>()).subEvent(SubEvent.builder().id(1L).build()).build();

        // When
        when(drawingLotteryRepository.findBySubEventIdAndSequence(any(), any())).thenReturn(Optional.of(drawingEvent));
        when(eventUserRepository.findByUserIdAndSubEventId(anyLong(), anyLong())).thenReturn(Optional.of(eventUser));
        when(eventUserRepository.save(any())).thenReturn(eventUser);

        DrawingScoreDto drawingScore = drawingLotteryService.getDrawingScore(user, drawingScoreRequest);

        // Then
        assertThat(drawingScore.getScore()).isInstanceOf(Double.class);
        assertThat(drawingScore.getScore()).isGreaterThan(90.0);
        log.info("drawingScore: {}", drawingScore);
    }

    @Test
    @DisplayName("드로잉 이벤트 게임 점수 채점 저득점")
    void getDrawingScore_success_low_score() {
        // Given
        List<PositionDto> positions = List.of(
                new PositionDto(673.5, 395),
                new PositionDto(675.5, 395),
                new PositionDto(681.5, 396),
                new PositionDto(683.5, 396),
                new PositionDto(687.5, 397),
                new PositionDto(691.5, 397),
                new PositionDto(694.5, 398),
                new PositionDto(698.5, 399),
                new PositionDto(702.5, 400),
                new PositionDto(705.5, 401),
                new PositionDto(707.5, 401),
                new PositionDto(709.5, 402),
                new PositionDto(712.5, 402),
                new PositionDto(714.5, 402),
                new PositionDto(716.5, 402),
                new PositionDto(718.5, 402),
                new PositionDto(719.5, 402),
                new PositionDto(721.5, 402),
                new PositionDto(724.5, 401),
                new PositionDto(727.5, 399),
                new PositionDto(730.5, 396),
                new PositionDto(734.5, 393),
                new PositionDto(738.5, 388),
                new PositionDto(742.5, 384),
                new PositionDto(746.5, 379),
                new PositionDto(750.5, 375),
                new PositionDto(751.5, 373),
                new PositionDto(757.5, 366),
                new PositionDto(758.5, 364),
                new PositionDto(760.5, 361),
                new PositionDto(761.5, 359),
                new PositionDto(762.5, 358),
                new PositionDto(762.5, 357),
                new PositionDto(763.5, 357),
                new PositionDto(763.5, 356),
                new PositionDto(763.5, 356),
                new PositionDto(763.5, 355),
                new PositionDto(763.5, 355),
                new PositionDto(763.5, 355),
                new PositionDto(764.5, 355),
                new PositionDto(764.5, 354),
                new PositionDto(764.5, 354),
                new PositionDto(764.5, 354),
                new PositionDto(764.5, 354),
                new PositionDto(764.5, 354),
                new PositionDto(765.5, 354),
                new PositionDto(770.5, 354),
                new PositionDto(776.5, 354),
                new PositionDto(784.5, 354),
                new PositionDto(790.5, 354),
                new PositionDto(801.5, 354),
                new PositionDto(811.5, 354),
                new PositionDto(820.5, 353),
                new PositionDto(824.5, 353),
                new PositionDto(831.5, 353),
                new PositionDto(836.5, 351),
                new PositionDto(841.5, 351),
                new PositionDto(845.5, 349),
                new PositionDto(847.5, 349),
                new PositionDto(851.5, 348),
                new PositionDto(853.5, 347),
                new PositionDto(856.5, 346),
                new PositionDto(858.5, 345),
                new PositionDto(860.5, 344),
                new PositionDto(862.5, 343),
                new PositionDto(863.5, 343),
                new PositionDto(865.5, 342),
                new PositionDto(866.5, 341),
                new PositionDto(866.5, 341),
                new PositionDto(867.5, 340),
                new PositionDto(867.5, 340),
                new PositionDto(868.5, 340),
                new PositionDto(868.5, 340),
                new PositionDto(868.5, 339),
                new PositionDto(868.5, 339),
                new PositionDto(868.5, 339),
                new PositionDto(868.5, 339),
                new PositionDto(868.5, 339),
                new PositionDto(868.5, 338),
                new PositionDto(868.5, 338),
                new PositionDto(868.5, 337),
                new PositionDto(868.5, 336),
                new PositionDto(868.5, 336),
                new PositionDto(868.5, 334),
                new PositionDto(868.5, 332),
                new PositionDto(868.5, 331),
                new PositionDto(868.5, 329),
                new PositionDto(867.5, 327),
                new PositionDto(866.5, 324),
                new PositionDto(865.5, 322),
                new PositionDto(865.5, 320),
                new PositionDto(864.5, 318),
                new PositionDto(864.5, 316),
                new PositionDto(863.5, 314),
                new PositionDto(863.5, 311),
                new PositionDto(862.5, 309),
                new PositionDto(862.5, 305),
                new PositionDto(861.5, 303),
                new PositionDto(861.5, 299),
                new PositionDto(861.5, 297),
                new PositionDto(859.5, 291),
                new PositionDto(859.5, 289),
                new PositionDto(858.5, 285),
                new PositionDto(857.5, 281),
                new PositionDto(856.5, 279),
                new PositionDto(856.5, 275),
                new PositionDto(854.5, 272),
                new PositionDto(853.5, 269),
                new PositionDto(851.5, 265),
                new PositionDto(851.5, 263),
                new PositionDto(849.5, 256),
                new PositionDto(848.5, 252),
                new PositionDto(846.5, 247),
                new PositionDto(845.5, 241),
                new PositionDto(843.5, 235),
                new PositionDto(842.5, 227),
                new PositionDto(841.5, 220),
                new PositionDto(841.5, 217),
                new PositionDto(839.5, 204),
                new PositionDto(838.5, 198),
                new PositionDto(837.5, 191),
                new PositionDto(837.5, 185),
                new PositionDto(837.5, 177),
                new PositionDto(837.5, 170),
                new PositionDto(837.5, 162),
                new PositionDto(837.5, 155),
                new PositionDto(837.5, 147),
                new PositionDto(837.5, 140),
                new PositionDto(837.5, 137),
                new PositionDto(837.5, 131),
                new PositionDto(838.5, 126),
                new PositionDto(839.5, 122),
                new PositionDto(840.5, 120),
                new PositionDto(840.5, 118),
                new PositionDto(841.5, 117),
                new PositionDto(841.5, 116),
                new PositionDto(841.5, 116),
                new PositionDto(842.5, 116),
                new PositionDto(842.5, 115),
                new PositionDto(842.5, 115),
                new PositionDto(840.5, 115),
                new PositionDto(838.5, 115),
                new PositionDto(836.5, 115),
                new PositionDto(833.5, 115),
                new PositionDto(828.5, 115),
                new PositionDto(823.5, 114),
                new PositionDto(818.5, 113),
                new PositionDto(812.5, 112),
                new PositionDto(808.5, 110),
                new PositionDto(805.5, 110),
                new PositionDto(800.5, 109),
                new PositionDto(795.5, 108),
                new PositionDto(788.5, 106),
                new PositionDto(784.5, 105),
                new PositionDto(781.5, 105),
                new PositionDto(777.5, 105),
                new PositionDto(774.5, 105),
                new PositionDto(770.5, 105),
                new PositionDto(767.5, 105),
                new PositionDto(764.5, 105),
                new PositionDto(762.5, 105),
                new PositionDto(760.5, 105),
                new PositionDto(756.5, 105),
                new PositionDto(755.5, 105),
                new PositionDto(753.5, 106),
                new PositionDto(752.5, 106));

        DrawingScoreRequest drawingScoreRequest = DrawingScoreRequest.builder()
                .positions(positions)
                .subEventId(1L)
                .sequence(1)
                .build();

        DrawingLotteryEvent drawingEvent = DrawingLotteryEvent.builder().id(1L).sequence(1).startPosX(1.0).startPosY(1.5).drawPointsJsonUrl("https://softeer-static.s3.ap-northeast-2.amazonaws.com/drawingLottery/answer/contour_points_01.json").build();

        User user = User.builder().id(1L).build();
        EventUser eventUser = EventUser.builder().user(user).scores(new HashMap<>()).subEvent(SubEvent.builder().id(1L).build()).build();

        // When
        when(drawingLotteryRepository.findBySubEventIdAndSequence(any(), any())).thenReturn(Optional.of(drawingEvent));
        when(eventUserRepository.findByUserIdAndSubEventId(anyLong(), anyLong())).thenReturn(Optional.of(eventUser));
        when(eventUserRepository.save(any())).thenReturn(eventUser);

        DrawingScoreDto drawingScore = drawingLotteryService.getDrawingScore(user, drawingScoreRequest);

        // Then
        assertThat(drawingScore.getScore()).isInstanceOf(Double.class);
        assertThat(drawingScore.getScore()).isLessThan(50.0);
        log.info("drawingScore: {}", drawingScore);
    }
}


