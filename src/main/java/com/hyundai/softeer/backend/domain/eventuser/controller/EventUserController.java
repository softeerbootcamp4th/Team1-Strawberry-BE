package com.hyundai.softeer.backend.domain.eventuser.controller;

import com.hyundai.softeer.backend.domain.eventuser.dto.EventUserInfoDto;
import com.hyundai.softeer.backend.domain.eventuser.service.EventUserService;
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
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/eventuser")
public class EventUserController {
    private final EventUserService eventUserService;

    @GetMapping("/info")
    @Operation(summary = "드로잉 추첨 이벤트 유저 정보 조회", description = """
            # 드로잉 추첨 이벤트 랜딩 페이지 조회
                        
            - 로그인 된 유저의 해당 이벤트 참여 정보를 함께 반환합니다.
             
            ## 응답
                        
            - 조회 성공 시 `200` 코드와 랜딩 페이지 정보를 반환합니다.
            - 이벤트 번호에 오류가 있을 경우 `404` 에러를 반환합니다.
             
            """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "드로잉 추첨 이벤트 랜딩 페이지 조회 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 이벤트 정보", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = BaseResponse.class), examples = @ExampleObject("{\"message\":\"드로잉 이벤트가 존재하지 않습니다.\",\"status\":404}"))}),
    })
    @SecurityRequirement(name = "access-token")
    public BaseResponse<EventUserInfoDto> getDrawingUserInfo(
            @Parameter(hidden = true) @CurrentUser User authenticatedUser,
            @RequestParam("subEventId") Long subEventId
    ) {
        return new BaseResponse<>(eventUserService.getEventUserInfo(authenticatedUser, subEventId));
    }
}
