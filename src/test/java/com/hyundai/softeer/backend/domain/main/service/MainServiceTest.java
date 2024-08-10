package com.hyundai.softeer.backend.domain.main.service;

import com.hyundai.softeer.backend.domain.event.entity.Event;
import com.hyundai.softeer.backend.domain.event.exception.EventNotFoundException;
import com.hyundai.softeer.backend.domain.event.repository.EventRepository;
import com.hyundai.softeer.backend.domain.main.dto.MainLandDto;
import com.hyundai.softeer.backend.domain.subevent.entity.SubEvent;
import com.hyundai.softeer.backend.domain.subevent.exception.SubEventNotFoundException;
import com.hyundai.softeer.backend.domain.subevent.repository.SubEventRepository;
import com.hyundai.softeer.backend.global.utils.DateUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MainServiceTest {

    @InjectMocks
    MainService mainService;

    @Mock
    EventRepository eventRepository;

    @Mock
    SubEventRepository subEventRepository;

    @Mock
    DateUtil dateUtil;

    @Test
    @DisplayName("메인 랜딩 페이지 api: 성공 시")
    void mainLandSuccessTest() {
        // given
        long eventId = 1L;

        when(eventRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(Event.testEventGenerator(eventId)));
        when(subEventRepository.findByEventId(any(Long.class))).thenReturn(SubEvent.subEventsGenerator(3L));
        when(dateUtil.findClosestSubEvent(any(List.class))).thenReturn(SubEvent.subEventGenerator(1L));
        when(dateUtil.startBetweenCurrentDiff(any(SubEvent.class))).thenReturn(1000L);

        // when
        MainLandDto mainLandDto = mainService.mainLand(eventId);

        // then
        assertThat(mainLandDto).isNotNull();
        assertThat(mainLandDto.getRemainSecond()).isEqualTo(1000L);
        assertThat(mainLandDto.getImgs()).isNotNull();
        assertThat(mainLandDto.getImgs().size()).isEqualTo(7);
    }

    @Test
    @DisplayName("메인 랜딩 페이지 api: subEvent가 존재하지 않을 때")
    void mainLandSubEventNotExistTest() {
        // given
        long eventId = 2L;

        when(eventRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(Event.testEventGenerator(eventId)));
        when(subEventRepository.findByEventId(any(Long.class))).thenReturn(SubEvent.subEventsGenerator(3L));
        when(dateUtil.findClosestSubEvent(any(List.class))).thenReturn(null);

        // when
        Assertions.assertThatThrownBy(() -> mainService.mainLand(eventId))
                .isInstanceOf(SubEventNotFoundException.class);
    }

    @Test
    @DisplayName("메인 랜딩 페이지 api: event가 존재하지 않을 때")
    void mainLandEventNotExistTest() {
        // given
        long eventId = 2L;

        when(eventRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        // when
        Assertions.assertThatThrownBy(() -> mainService.mainLand(eventId))
                .isInstanceOf(EventNotFoundException.class);
    }
}