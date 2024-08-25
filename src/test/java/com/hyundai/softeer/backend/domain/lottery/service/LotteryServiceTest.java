package com.hyundai.softeer.backend.domain.lottery.service;

import com.hyundai.softeer.backend.domain.eventuser.entity.EventUser;
import com.hyundai.softeer.backend.domain.lottery.drawing.service.DrawingLotteryService;
import com.hyundai.softeer.backend.domain.subevent.dto.LotteryScoreWeight;
import com.hyundai.softeer.backend.domain.subevent.dto.WinnerCandidate;
import com.hyundai.softeer.backend.domain.subevent.dto.WinnerInfo;
import com.hyundai.softeer.backend.domain.user.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@Slf4j
class LotteryServiceTest {
    @InjectMocks
    private DrawingLotteryService lotteryService;

    @Test
    @DisplayName("당첨자 선정")
    void getWinners() {
        // given
        Set<EventUser> users = Set.of(
                EventUser.builder().id(1L).user(User.builder().id(1L).build()).build(),
                EventUser.builder().id(2L).user(User.builder().id(1L).build()).build(),
                EventUser.builder().id(3L).user(User.builder().id(1L).build()).build(),
                EventUser.builder().id(4L).user(User.builder().id(1L).build()).build(),
                EventUser.builder().id(5L).user(User.builder().id(1L).build()).build());

        Map<Integer, WinnerInfo> winnersMeta = Map.of(
                1, new WinnerInfo(3, 1L, 1));
        LotteryScoreWeight scoreWeight = new LotteryScoreWeight(1.0, 1.0, 1.0, 1.0);

        // when

        List<WinnerCandidate> winners = lotteryService.getWinners(users, scoreWeight, 3);
        log.info("당첨자: {}", winners);

        // then
        assertThat(winners).hasSize(3);
    }
}