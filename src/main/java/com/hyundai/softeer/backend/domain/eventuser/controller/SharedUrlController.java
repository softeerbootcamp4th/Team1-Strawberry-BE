package com.hyundai.softeer.backend.domain.eventuser.controller;

import com.hyundai.softeer.backend.domain.eventuser.dto.RedirectUrlDto;
import com.hyundai.softeer.backend.domain.eventuser.dto.SharedUrlRequest;
import com.hyundai.softeer.backend.domain.eventuser.service.EventUserService;
import com.hyundai.softeer.backend.global.dto.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/url")
@Tag(name = "Shared Url")
public class SharedUrlController {
    private final EventUserService eventUserService;

    @GetMapping("/{sharedUrl}")
    @Operation(summary = "공유 주소로 Redirect URL 반환", description = """
            # 공유 주소로 Redirect URL 반환
                    
            - 공유 주소를 통해 이벤트 참가 유저 정보를 조회합니다.
                    
            ## 응답
                    
            - 조회 성공 시 `200` 코드와 Redirect URL을 반환합니다.
            - 공유 주소에 오류가 있을 경우 `404` 에러를 반환합니다.                        
            """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이벤트 참가 유저 정보 조회 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 이벤트 참가자", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = BaseResponse.class), examples = @ExampleObject("{\"message\":\"잘못된 공유 주소입니다.\",\"status\":404}"))}),
    })
    public BaseResponse<RedirectUrlDto> getSharedUrl(
            @PathVariable @Validated SharedUrlRequest sharedUrl
    ) {
        return new BaseResponse<>(eventUserService.getRedirectUrl(sharedUrl));
    }
}
