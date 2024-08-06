package com.hyundai.softeer.backend.global.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hyundai.softeer.backend.domain.subevent.dto.WinnerInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ParseUtilTest {

    @Test
    @DisplayName("당첨자 정보 파싱 테스트")
    void winnerInfoTest() throws JsonProcessingException {
        // given
        Map<String, Object> winnersInfo = Map.of(
                "1", Map.of("winnerCount", 1, "prizeId", 1L),
                "2", Map.of("winnerCount", 2, "prizeId", 2L),
                "3", Map.of("winnerCount", 3, "prizeId", 3L)
        );

        // when
        Map<Integer, WinnerInfo> winnersMetaInfo = ParseUtil.parseWinnersMeta(winnersInfo);

        // then
        assertThat(winnersMetaInfo).isNotEmpty();
        assertThat(winnersMetaInfo.get(1)).isEqualTo(new WinnerInfo(1, 1L, 1));
        assertThat(winnersMetaInfo.get(2)).isEqualTo(new WinnerInfo(2, 2L, 2));
        assertThat(winnersMetaInfo.get(3)).isEqualTo(new WinnerInfo(3, 3L, 3));
    }
//    @Getter
//    @AllArgsConstructor
//    public class WinnerInfo {
//        private int winnerCount;
//        private int prizeId;
//        private String prizeImg;
//    }
}
