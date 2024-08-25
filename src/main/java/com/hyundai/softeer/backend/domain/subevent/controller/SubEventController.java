package com.hyundai.softeer.backend.domain.subevent.controller;

import com.hyundai.softeer.backend.domain.event.service.EventService;
import com.hyundai.softeer.backend.domain.subevent.dto.*;
import com.hyundai.softeer.backend.domain.subevent.service.SubEventService;
import com.hyundai.softeer.backend.domain.winner.dto.WinnerInfoDto;
import com.hyundai.softeer.backend.global.dto.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/subevent")
public class SubEventController {
    private final EventService eventService;
    private final SubEventService subEventService;

    @GetMapping("/winner")
    @Tag(name = "Admin")
    @Operation(summary = "드로잉 추첨 이벤트 당첨자 조회", description = """
            # 드로잉 추첨 이벤트 당첨자 조회
                        
            - 드로잉 추첨 이벤트의 당첨자를 조회합니다.
                        
            ## 응답
                        
            - 조회 성공 시 `200` 코드와 랜딩 페이지 정보를 반환합니다.
            - 이벤트 번호에 오류가 있을 경우 `404` 에러를 반환합니다.
             
            """)
    @ApiResponses(value = {})
    @SecurityRequirement(name = "access-token")
    public BaseResponse<List<WinnerInfoDto>> getWinnerList(
            @Validated SubEventRequest subEventRequest
    ) {
        return new BaseResponse<>(subEventService.getWinners(subEventRequest));
    }

    @GetMapping("/draw")
    @Tag(name = "Admin")
    @Operation(summary = "드로잉 추첨 이벤트 추첨", description = """
            # 드로잉 추첨 이벤트 추첨
                        
            - 드로잉 추첨 이벤트의 당첨자를 추첨합니다.
                        
            ## 응답
                        
            - 조회 성공 시 `200` 코드와 랜딩 페이지 정보를 반환합니다.
            - 이벤트 번호에 오류가 있을 경우 `404` 에러를 반환합니다.
             
            """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "드로잉 추첨 이벤트 추첨 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 이벤트 정보", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = BaseResponse.class), examples = @ExampleObject("{\"message\":\"드로잉 이벤트가 존재하지 않습니다.\",\"status\":404}"))}),
    })
    @SecurityRequirement(name = "access-token")
    public BaseResponse<List<WinnerCandidate>> drawWinner(
            @Validated SubEventRequest subEventRequest
    ) {
        return new BaseResponse<>(subEventService.drawWinner(subEventRequest));
    }

    @GetMapping("/list")
    public Page<SubEventSimpleDto> getEvents(
            @RequestParam String eventId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        return subEventService.getSubEvents(Long.parseLong(eventId), page, size);
    }

    @PutMapping("/{subEventId}")
    public void updateSubEvent(
            @PathVariable Long subEventId,
            @RequestBody UpdateSubEventPeriodRequest updateSubEventPeriodRequest
    ) {
        subEventService.updateSubEvent(subEventId, updateSubEventPeriodRequest);
    }

    @PutMapping("/{subEventId}/winner")
    public void updateSubEventWinnerMeta(
            @PathVariable Long subEventId,
            @RequestBody List<UpdateWinnerMetaRequest> updateWinnerMetaRequests
    ) {
        subEventService.updateSubEventWinnerMeta(subEventId, updateWinnerMetaRequests);
    }
}
