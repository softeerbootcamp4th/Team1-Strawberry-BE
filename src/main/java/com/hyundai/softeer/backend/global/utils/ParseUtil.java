package com.hyundai.softeer.backend.global.utils;

import com.hyundai.softeer.backend.domain.lottery.drawing.dto.PositionDto;
import com.hyundai.softeer.backend.domain.subevent.dto.WinnerInfo;
import com.hyundai.softeer.backend.global.exception.error.JsonParseException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    public static List<PositionDto> parsePositionsFromJson(String jsonUrl) {
        List<PositionDto> positions = new ArrayList<>();

        // JSON 데이터 읽기
        try (InputStream is = new URL(jsonUrl).openStream()) {
            StringBuilder jsonBuilder = new StringBuilder();
            int byteRead;
            while ((byteRead = is.read()) != -1) {
                jsonBuilder.append((char) byteRead);
            }

            // JSON 데이터 파싱
            JSONArray jsonArray = new JSONArray(jsonBuilder.toString());

            // Position 객체 리스트 생성
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject point = jsonArray.getJSONObject(i);
                double x = point.getDouble("x");
                double y = point.getDouble("y");
                positions.add(new PositionDto(x, y));
            }
        } catch (IOException e) {
            throw new JsonParseException();
        }

        return positions;
    }
}
