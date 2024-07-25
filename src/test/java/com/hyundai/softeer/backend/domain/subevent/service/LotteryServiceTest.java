package com.hyundai.softeer.backend.domain.subevent.service;

import com.hyundai.softeer.backend.domain.eventuser.entity.EventUser;
import com.hyundai.softeer.backend.domain.subevent.dto.LotteryScoreWeight;
import com.hyundai.softeer.backend.domain.subevent.dto.WinnerCandidate;
import com.hyundai.softeer.backend.domain.subevent.dto.WinnerInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
class LotteryServiceTest {
    @InjectMocks
    private LotteryService lotteryService;

    @Test
    @DisplayName("당첨자 메타 데이터 파싱")
    void parseWinnerMeta() {
        // given
        String winnerMeta = "{'1':[1,1],'2':[2,2],'3':[3,3],'4':[4,4],'5':[5,5],'6':[6,6],'7':[7,7],'8':[8,8],'9':[9,9],'10':[10,10]}";
        // when
        Map<Integer, WinnerInfo> integerWinnerInfoMap = lotteryService.parseWinnersMeta(winnerMeta);

        // then
        for (int i = 1; i <= 10; i++) {
            WinnerInfo winnerInfo = integerWinnerInfoMap.get(i);
            Assertions.assertNotNull(winnerInfo);
            Assertions.assertEquals(winnerInfo.getWinnerCount(), i);
            Assertions.assertEquals(winnerInfo.getPrizeId(), i);
        }
    }

    @Test
    @DisplayName("당첨자 선정")
    void getWinners() {
        // given
        List<EventUser> users = List.of(
                EventUser.builder().id(1L).build(),
                EventUser.builder().id(2L).build(),
                EventUser.builder().id(3L).build(),
                EventUser.builder().id(4L).build(),
                EventUser.builder().id(5L).build());

        Map<Integer, WinnerInfo> winnersMeta = Map.of(
                1, new WinnerInfo(3, 1));
        LotteryScoreWeight scoreWeight = new LotteryScoreWeight(1.0, 1.0, 1.0, 1.0);

        // when

        List<WinnerCandidate> winners = lotteryService.getWinners(winnersMeta, users, scoreWeight);

        // then

        Assertions.assertEquals(3, winners.size());
    }
}