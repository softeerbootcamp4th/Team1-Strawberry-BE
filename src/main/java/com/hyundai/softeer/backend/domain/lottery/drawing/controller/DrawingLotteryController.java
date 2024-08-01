package com.hyundai.softeer.backend.domain.lottery.drawing.controller;

import com.hyundai.softeer.backend.domain.lottery.drawing.dto.DrawingLotteryLandResponseDto;
import com.hyundai.softeer.backend.domain.lottery.drawing.service.DrawingLotteryService;
import com.hyundai.softeer.backend.domain.user.entity.User;
import com.hyundai.softeer.backend.global.dto.BaseResponse;
import com.hyundai.softeer.backend.global.jwt.CurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/lottery/drawing")
@Tag(name = "Drawing Lottery")
public class DrawingLotteryController {
    private final DrawingLotteryService drawingLotteryService;

    @GetMapping("/land")
    @Operation(summary = "드로잉 추첨 이벤트 랜딩 페이지 조회", description = """
            # 드로잉 추첨 이벤트 랜딩 페이지 조회
                        
            - 드로잉 추첨 이벤트 랜딩 페이지의 기본 정보를 조회합니다.
            - 로그인 된 유저의 해당 이벤트 참여 정보를 함께 반환합니다.
             
            ## 응답
                        
            - 조회 성공 시 `200` 코드와 랜딩 페이지 정보를 반환합니다.
            - 이벤트 번호에 오류가 있을 경우 `404` 에러를 반환합니다.
             
            """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "드로잉 추첨 이벤트 랜딩 페이지 조회 성공", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = DrawingLotteryLandResponseDto.class))}),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 이벤트 정보", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = BaseResponse.class), examples = @ExampleObject("{\"message\":\"드로잉 이벤트가 존재하지 않습니다.\",\"status\":404}"))}),
    })
    @SecurityRequirement(name = "access-token")
    public DrawingLotteryLandResponseDto getQuizLandingPage(
            @Parameter(hidden = true) @CurrentUser User user,
            @RequestParam("eventId") Long eventId
    ) {

        return drawingLotteryService.getDrawingLotteryLand(user, eventId);
    }
}
