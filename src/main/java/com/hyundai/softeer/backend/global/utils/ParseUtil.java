package com.hyundai.softeer.backend.global.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hyundai.softeer.backend.domain.firstcomeevent.quiz.exception.JsonParseException;
import com.hyundai.softeer.backend.domain.subevent.dto.WinnerInfo;

import java.util.HashMap;
import java.util.Map;

public class ParseUtil {

    public static Map<Integer, WinnerInfo> parseWinnersMeta(String winnersMeta) {
        Map<Integer, WinnerInfo> resultMap = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode rootNode;

        // JSON 문자열을 JsonNode로 변환
        try {
            rootNode = objectMapper.readTree(winnersMeta.replace("'", "\""));
        } catch (JsonProcessingException e) {
            throw new JsonParseException();
        }

        // 각 키와 값 파싱
        rootNode.fields().forEachRemaining(entry -> {
            Integer rank = Integer.valueOf(entry.getKey());
            JsonNode values = entry.getValue();

            int winnerCount = values.get(0).asInt();
            long prizeId = values.get(1).asLong();
            int _rank = values.get(2).asInt();

            resultMap.put(rank, new WinnerInfo(winnerCount, prizeId, _rank));
        });
        return resultMap;
    }
}