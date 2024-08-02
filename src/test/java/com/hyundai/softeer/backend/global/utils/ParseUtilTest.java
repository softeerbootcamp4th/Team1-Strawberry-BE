package com.hyundai.softeer.backend.global.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hyundai.softeer.backend.domain.subevent.dto.WinnerInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ParseUtilTest {

    @Test
    @DisplayName("당첨자 정보 파싱 테스트")
    void winnerInfoTest() throws JsonProcessingException {
        // given
        String winnersInfo = "{" +
                "'1': [1, 1, 1]," +
                "'2': [2, 2, 2]," +
                "'3': [3, 3, 3]" +
                "}";

        // when
        Map<Integer, WinnerInfo> integerWinnerInfoMap = ParseUtil.parseWinnersMeta(winnersInfo);

        // then
        assertThat(integerWinnerInfoMap).isNotEmpty();
        assertThat(integerWinnerInfoMap.get(1)).isEqualTo(new WinnerInfo(1, 1L, 1));
        assertThat(integerWinnerInfoMap.get(2)).isEqualTo(new WinnerInfo(2, 2L, 2));
        assertThat(integerWinnerInfoMap.get(3)).isEqualTo(new WinnerInfo(3, 3L, 3));
    }
//    @Getter
//    @AllArgsConstructor
//    public class WinnerInfo {
//        private int winnerCount;
//        private int prizeId;
//        private String prizeImg;
//    }
}