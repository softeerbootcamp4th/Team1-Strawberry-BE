package com.hyundai.softeer.backend.domain.expectation.controller;

import com.hyundai.softeer.backend.domain.expectation.dto.ExpectationsRequest;
import com.hyundai.softeer.backend.domain.expectation.dto.ExpectationsResponseDto;
import com.hyundai.softeer.backend.domain.expectation.dto.ExpectationPageRequest;
import com.hyundai.softeer.backend.domain.expectation.dto.ExpectationPageResponseDto;
import com.hyundai.softeer.backend.domain.expectation.service.ExpectationService;
import com.hyundai.softeer.backend.global.dto.BaseResponse;
import com.hyundai.softeer.backend.global.exception.ApiErrorResponse;
import com.hyundai.softeer.backend.domain.expectation.dto.*;
import com.hyundai.softeer.backend.domain.user.entity.User;
import com.hyundai.softeer.backend.global.jwt.CurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.Null;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ExpectationController {

    private final ExpectationService expectationService;

    @Value("${properties.event-id}")
    private Long eventId;

    @Operation(summary = "기대평 랜딩 페이지 api", description = """
    기대평 페이지의 데이터를 반환하는 api
        
    응답
    - 파라미터의 값이 존재하지 않거나 올바르지 않은 타입인 경우 `400`을 응답한다.
    - 이벤트가 존재하지 않는다면 `404`로 응답한다.
    - 성공 시 `200`을 응답한다.
    """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상 반환 시", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "쿼리 파라미터의 타입이 잘못되었거나 존재하지 않을 때", content = {@Content(schema = @Schema(implementation = ApiErrorResponse.class))}),
            @ApiResponse(responseCode = "404", description = "해당하는 이벤트가 존재하지 않는 경우", content = {@Content(schema = @Schema(implementation = ApiErrorResponse.class))}),
    })
    @GetMapping("/api/v1/expectation")
    public BaseResponse<ExpectationPageResponseDto> expectationLandApi() {
        ExpectationPageResponseDto expectationPage = expectationService.getExpectationPage(eventId);

        return new BaseResponse<>(expectationPage);
    }

    @Operation(summary = "기대평 페이지네이션 api", description = """
    기대평의 페이지네이션을 반환하는 api
        
    응답
    - 파라미터의 값이 존재하지 않거나 올바르지 않은 타입인 경우 `400`을 응답한다.
    - 이벤트가 존재하지 않는다면 `404`로 응답한다.
    - 요청한 페이지 번호가 최대 페이지 번호를 넘을 경우 `404`로 응답한다.
    - 성공 시 `200`을 응답한다.
        
    주의
    - pageSequence는 반드시 0-based로 요청해야한다.
    - 예를 들어, 1번 페이지는 pageSequence=0 으로 요청해야한다.
    """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상 반환 시", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "쿼리 파라미터의 타입이 잘못되었거나 존재하지 않을 때", content = {@Content(schema = @Schema(implementation = ApiErrorResponse.class))}),
            @ApiResponse(responseCode = "404", description = "해당하는 이벤트 존재하지 않는 경우 또는 최대 페이지 번호를 넘을 경우", content = {@Content(schema = @Schema(implementation = ApiErrorResponse.class))}),
    })
    @GetMapping("/api/v1/expectation/page")
    public BaseResponse<ExpectationsResponseDto> expectationsApi(
            @Validated ExpectationsRequest expectationsRequest
    ) {
        ExpectationsResponseDto expectations = expectationService.getExpectations(expectationsRequest, eventId);
        return new BaseResponse<>(expectations);
    }

    @Operation(summary = "기대평 제출 api", description = """
    기대평을 제출하는 api
        
    응답
    - 파라미터의 값이 존재하지 않거나 올바르지 않은 타입인 경우 `400`을 응답한다.
    - 정상적으로 기대평이 저장된 경우 `201`을 반환한다.
    """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "정상 반환 시", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "쿼리 파라미터의 타입이 잘못되었거나 존재하지 않을 때", content = {@Content(schema = @Schema(implementation = ApiErrorResponse.class))}),
    })
    @PostMapping("/api/v1/expectation")
    @ResponseStatus(HttpStatus.CREATED)
    public BaseResponse<Null> expectationRegisterApi(
            @RequestBody @Validated ExpectationRegisterRequest expectationRegisterRequest,
            @Parameter(hidden = true) @CurrentUser User authenticatedUser
    ) {
        expectationService.expectationRegisterApi(expectationRegisterRequest, eventId, authenticatedUser);

        return new BaseResponse<>(HttpStatus.CREATED.value(), "기대평 생성되었습니다.", null);
    }
}
