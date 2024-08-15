package com.hyundai.softeer.backend.domain.eventuser.controller;

import com.hyundai.softeer.backend.domain.eventuser.dto.EventUserInfoDto;
import com.hyundai.softeer.backend.domain.eventuser.dto.EventUserPageRequest;
import com.hyundai.softeer.backend.domain.eventuser.dto.EventUserPageResponseDto;
import com.hyundai.softeer.backend.domain.eventuser.dto.SharedUrlDto;
import com.hyundai.softeer.backend.domain.eventuser.service.EventUserService;
import com.hyundai.softeer.backend.domain.subevent.dto.SubEventRequest;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/eventuser")
@Tag(name = "Event User")
public class EventUserController {
    private final EventUserService eventUserService;

    @Value("${properties.event-id}")
    private Long eventId;

    @GetMapping("/info")
    @Operation(summary = "이벤트 참가 유저 정보 조회", description = """
            # 이벤트 참가 유저 정보 조회
                        
            - 로그인 된 유저의 해당 이벤트 참여 정보를 함께 반환합니다.
             
            ## 응답
                        
            - 조회 성공 시 `200` 코드와 랜딩 페이지 정보를 반환합니다.
            - 이벤트 번호에 오류가 있을 경우 `404` 에러를 반환합니다.
             
            """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이벤트 참가 유저 정보 조회 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 이벤트 참가자", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = BaseResponse.class), examples = @ExampleObject("{\"message\":\"해당 이벤트 참가자를 찾을 수 없습니다.\",\"status\":404}"))}),
    })
    @SecurityRequirement(name = "access-token")
    public BaseResponse<EventUserInfoDto> getDrawingUserInfo(
            @Parameter(hidden = true) @CurrentUser User authenticatedUser,
            @Validated SubEventRequest subEventRequest
    ) {
        return new BaseResponse<>(eventUserService.getEventUserInfo(authenticatedUser, subEventRequest.getSubEventId()));
    }

    @GetMapping("/shared-url")
    @Operation(summary = "공유 URL 조회 및 생성", description = """
            # 공유 URL 조회 및 생성
                        
            - 로그인 된 유저의 해당 이벤트 공유 URL 과 최고 점수를 반환합니다.
                        
            ## 응답
                        
            - 조회 성공 시 `200` 코드와 공유 URL 정보를 반환합니다.
            - 게임 점수가 없을 시 `400` 에러를 반환합니다.
            - 이벤트 번호와 로그인 된 사용자와 일치하는 정보가 없을 시 `404` 에러를 반환합니다.
                        
            """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이벤트 참가 유저 정보 조회 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "게임을 플레이하지 않은 사용자", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = BaseResponse.class), examples = @ExampleObject("{\"message\":\"아직 한 번도 참여하지 않은 사용자입니다.\",\"status\":400}"))}),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 이벤트 참가자", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = BaseResponse.class), examples = @ExampleObject("{\"message\":\"해당 이벤트 참가자를 찾을 수 없습니다.\",\"status\":404}"))}),
    })
    @SecurityRequirement(name = "access-token")
    public BaseResponse<SharedUrlDto> getSharedUrl(
            @Parameter(hidden = true) @CurrentUser User authenticatedUser,
            @Validated SubEventRequest subEventRequest
    ) {
        return new BaseResponse<>(eventUserService.getSharedUrl(authenticatedUser, subEventRequest));
    }

    @GetMapping("/page")
    @Operation(summary = "어드민 이벤트 유저 조회", description = """
            # 어드민 이벤트 유저 조회
            - 페이지 별 이벤트 참가 유저를 조회합니다.
                        
            ## 응답
                        
            - 조회 성공 시 `200`를 반환합니다.
            - 파라미터가 없거나 올바른 타입이 아닌 경우 `400` 에러를 반환합니다.
            - 존재하는 최대 페이지 수를 넘는 경우 `404` 에러를 반환합니다.
            
            ## 주의
            
            - 페이지 번호는 0-based로 받습니다.
            - 예를 들어 페이지 1번은 pageSequence=0을 요청해야합니다.
            """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이벤트 참가 유저 정보 조회 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "파라미터가 올바르지 않을 때", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = BaseResponse.class), examples = @ExampleObject("{\"message\":\"올바르지 않은 쿼리 파라미터입니다.\",\"status\":400}"))}),
            @ApiResponse(responseCode = "404", description = "최대 페이지수를 초과", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = BaseResponse.class), examples = @ExampleObject("{\"message\":\"최대 페이지를 넘어섰습니다.\",\"status\":404}"))}),
    })
    public BaseResponse<EventUserPageResponseDto> getEventUserPage(
            @Validated EventUserPageRequest eventUserPageRequest
            ) {
        EventUserPageResponseDto eventUserPage = eventUserService.getEventUserPage(eventUserPageRequest);

        return new BaseResponse<>(eventUserPage);
    }
}
