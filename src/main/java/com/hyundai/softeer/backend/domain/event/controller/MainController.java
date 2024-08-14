package com.hyundai.softeer.backend.domain.event.controller;

import com.hyundai.softeer.backend.domain.event.dto.MainLandDto;
import com.hyundai.softeer.backend.domain.event.service.MainService;
import com.hyundai.softeer.backend.global.dto.BaseResponse;
import com.hyundai.softeer.backend.global.exception.ApiErrorResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import jakarta.validation.constraints.NotNull;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MainController {

    private final MainService mainService;

    @Value("${properties.event-id}")
    private Long eventId;

    @Operation(summary = "메인 랜딩 페이지 api", description = """
            메인 랜딩 페이지 api
            - 퀴즈 이벤트까지 남은 시간과 이벤트 관련 이미지를 반환합니다.
                
            ## 응답
            - 이벤트 기간 내 모든 퀴즈 이벤트가 끝난 경우 `400`이 반환됩니다.
            - 정상 반환 시 `200`이 반환됩니다.
            """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상 반환 시", useReturnTypeSchema = true, content = {@Content(examples = @ExampleObject("{\n" +
                    "    \"status\": 200,\n" +
                    "    \"message\": \"OK\",\n" +
                    "    \"data\": {\n" +
                    "        \"remainSecond\": 0,\n" +
                    "        \"imgs\": {\n" +
                    "            \"mainImgUrl\": \"www.mainimg.com\",\n" +
                    "            \"quizMainImg\": \"www.quizmain.com\",\n" +
                    "            \"eventInfoImg\": \"www.eventinfo.com\",\n" +
                    "            \"quizPrizeImg\": \"www.qprize.com\",\n" +
                    "            \"drawingMainImg\": \"www.drawingmain.com\",\n" +
                    "            \"scrolledImgUrl\": \"www.scroll.com\",\n" +
                    "            \"drawingPrizeImg\": \"www.drawingprize.com\"\n" +
                    "        },\n" +
                    "        \"quizEventStartAt\": [\n" +
                    "            {\n" +
                    "                \"isStarted\": true,\n" +
                    "                \"startAt\": \"2024-08-06T10:30:00\"\n" +
                    "            },\n" +
                    "            {\n" +
                    "                \"isStarted\": true,\n" +
                    "                \"startAt\": \"2024-08-12T10:30:00\"\n" +
                    "            },\n" +
                    "            {\n" +
                    "                \"isStarted\": false,\n" +
                    "                \"startAt\": \"2024-08-19T10:30:00\"\n" +
                    "            }\n" +
                    "        ]\n" +
                    "    }\n" +
                    "}"))}),
            @ApiResponse(responseCode = "404", description = "퀴즈x", content = {@Content(schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject("{\"message\":\"퀴즈 이벤트가 존재하지 않습니다.\",\"status\":404}"))})
    })
    @GetMapping("/api/v1/land")
    public BaseResponse<MainLandDto> mainLandApi() {

        MainLandDto mainLandDto = mainService.mainLand(eventId);

        return new BaseResponse<>(mainLandDto);
    }
}
