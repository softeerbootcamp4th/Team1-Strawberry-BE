package com.hyundai.softeer.backend.global.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hyundai.softeer.backend.domain.lottery.drawing.dto.PositionDto;
import com.hyundai.softeer.backend.domain.subevent.dto.WinnerInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class ParseUtilTest {

    @Test
    @DisplayName("당첨자 정보 파싱 테스트")
    void winnerInfoTest() throws JsonProcessingException {
        // given
        Map<String, Object> winnersInfo = Map.of(
                "1", Map.of("winnerCount", 1, "prizeId", 1),
                "2", Map.of("winnerCount", 2, "prizeId", 2),
                "3", Map.of("winnerCount", 3, "prizeId", 3)
        );

        // when
        Map<Integer, WinnerInfo> winnersMetaInfo = ParseUtil.parseWinnersMeta(winnersInfo);

        // then
        assertThat(winnersMetaInfo).isNotEmpty();
        assertThat(winnersMetaInfo.get(1)).isEqualTo(new WinnerInfo(1, 1L, 1));
        assertThat(winnersMetaInfo.get(2)).isEqualTo(new WinnerInfo(2, 2L, 2));
        assertThat(winnersMetaInfo.get(3)).isEqualTo(new WinnerInfo(3, 3L, 3));
    }

    @Test
    @DisplayName("Json 파싱 테스트")
    void jsonParsingTest() {
        // given
        String jsonUrl = "https://softeer-static.s3.ap-northeast-2.amazonaws.com/drawingLottery/answer/contour_points_01.json";

        // when
        List<PositionDto> positions = ParseUtil.parsePositionsFromJson(jsonUrl);

        // then
        assertThat(positions).isNotEmpty();

        log.info("positions: {}", positions);
    }
}
