package com.hyundai.softeer.backend.global.utils;

import com.hyundai.softeer.backend.domain.subevent.dto.WinnerInfo;

import java.util.HashMap;
import java.util.Map;

public class ParseUtil {
    public static Map<Integer, WinnerInfo> parseWinnersMeta(Map<String, Object> winnersMeta) {
        Map<Integer, WinnerInfo> winners = new HashMap<>();

        for (Map.Entry<String, Object> entry : winnersMeta.entrySet()) {
            // 키를 Integer로 변환
            int key = Integer.parseInt(entry.getKey());

            // 값을 Map<String, Object>로 변환
            Map<String, Object> value = (Map<String, Object>) entry.getValue();

            // winnerCount와 prizeId를 추출
            int winnerCount = (int) value.get("winnerCount");
            int prizeId = (int) value.get("prizeId");

            // WinnerInfo 객체 생성
            WinnerInfo winnerInfo = new WinnerInfo(winnerCount, prizeId, key);
            // 결과 맵에 추가
            winners.put(key, winnerInfo);
        }

        return winners;
    }
}
