package com.hyundai.softeer.backend.domain.lottery.drawing.controller;

import com.hyundai.softeer.backend.domain.lottery.drawing.dto.DrawingInfoDtos;
import com.hyundai.softeer.backend.domain.lottery.drawing.dto.DrawingLotteryLandDto;
import com.hyundai.softeer.backend.domain.lottery.drawing.dto.DrawingScoreDto;
import com.hyundai.softeer.backend.domain.lottery.drawing.dto.DrawingScoreRequest;
import com.hyundai.softeer.backend.domain.lottery.drawing.service.DrawingLotteryService;
import com.hyundai.softeer.backend.domain.lottery.dto.RankDto;
import com.hyundai.softeer.backend.domain.subevent.dto.SubEventRequest;
import com.hyundai.softeer.backend.global.dto.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/lottery/drawing")
public class DrawingLotteryController {
    public static final int RANK_COUNT = 20;

    private final DrawingLotteryService drawingLotteryService;

    @Value("${properties.event-id}")
    private Long eventId;

    @GetMapping("/land")
    @Tag(name = "Drawing Lottery")
    @Operation(summary = "드로잉 추첨 이벤트 랜딩 페이지 조회", description = """
            # 드로잉 추첨 이벤트 랜딩 페이지 조회
                        
            - 드로잉 추첨 이벤트 랜딩 페이지의 기본 정보를 조회합니다.
             
            ## 응답
                        
            - 조회 성공 시 `200` 코드와 랜딩 페이지 정보를 반환합니다.
            - 이벤트 번호에 오류가 있을 경우 `404` 에러를 반환합니다.
             
            """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "드로잉 추첨 이벤트 랜딩 페이지 조회 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 이벤트 정보", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = BaseResponse.class), examples = @ExampleObject("{\"message\":\"드로잉 이벤트가 존재하지 않습니다.\",\"status\":404}"))}),
    })
    public BaseResponse<DrawingLotteryLandDto> getDrawingLandingPage() {
        return new BaseResponse<>(drawingLotteryService.getDrawingLotteryLand(eventId));
    }

    @GetMapping("/rank")
    @Tag(name = "Drawing Lottery")
    @Operation(summary = "드로잉 추첨 이벤트 랭킹 조회", description = """
            # 드로잉 추첨 이벤트 랭킹 조회
                        
            - 드로잉 추첨 이벤트의 랭킹을 조회합니다.
                        
            ## 응답
                        
            - 조회 성공 시 `200` 코드와 랜딩 페이지 정보를 반환합니다.
            - 이벤트 번호에 오류가 있을 경우 `404` 에러를 반환합니다.
             
            """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "드로잉 추첨 이벤트 랭킹 조회 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 이벤트 정보", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = BaseResponse.class), examples = @ExampleObject("{\"message\":\"드로잉 이벤트가 존재하지 않습니다.\",\"status\":404}"))}),
    })
    public BaseResponse<List<RankDto>> getRankList(
            @Validated SubEventRequest subEventRequest
    ) {
        return new BaseResponse<>(drawingLotteryService.getRankList(subEventRequest, RANK_COUNT));
    }

    @GetMapping("")
    @Tag(name = "Drawing Lottery")
    @Operation(summary = "드로잉 추첨 이벤트 게임 정보 조회", description = """
            # 드로잉 추첨 이벤트 게임 정보 조회
                        
            - 드로잉 추첨 이벤트의 게임 정보를 조회합니다.
                        
            ## 응답
                        
            - 조회 성공 시 `200` 코드와 게임 정보를 반환합니다.
            - 이벤트 번호에 오류가 있을 경우 `404` 에러를 반환합니다.             
            """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "드로잉 추첨 이벤트 랭킹 조회 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 이벤트 정보", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = BaseResponse.class), examples = @ExampleObject("{\"message\":\"드로잉 이벤트가 존재하지 않습니다.\",\"status\":404}"))}),
    })
    public BaseResponse<DrawingInfoDtos> getDrawingGameInfo(
            @Validated SubEventRequest subEventRequest
    ) {
        return new BaseResponse<>(drawingLotteryService.getDrawingGameInfo(subEventRequest));
    }

    @PostMapping("")
    @Tag(name = "Drawing Lottery")
    @Operation(summary = "드로잉 추첨 이벤트 게임 제출 및 채점", description = """
            # 드로잉 추첨 이벤트 게임 제출 및 채점
                        
            - 드로잉 추첨 이벤트의 게임 결과를 제출하고 채점합니다.
                        
            ## 응답
            
            - 채점 성공 시 `200` 코드와 채점 정보를 반환합니다.
            - 이벤트 번호에 오류가 있을 경우 `404` 에러를 반환합니다.             
            """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "드로잉 추첨 이벤트 게임 제출 및 채점 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 이벤트 정보", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = BaseResponse.class), examples = @ExampleObject("{\"message\":\"드로잉 이벤트가 존재하지 않습니다.\",\"status\":404}"))}),
    })
    public BaseResponse<DrawingScoreDto> getDrawingScore(
            @RequestBody @Validated DrawingScoreRequest drawingScoreRequest
            ) {
        return new BaseResponse<>(drawingLotteryService.getDrawingScore(drawingScoreRequest));
    }
}
