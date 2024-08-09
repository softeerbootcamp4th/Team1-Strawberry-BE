package com.hyundai.softeer.backend.domain.firstcomeevent.quiz.controller;

import com.hyundai.softeer.backend.domain.firstcomeevent.quiz.dto.*;
import com.hyundai.softeer.backend.domain.firstcomeevent.quiz.service.QuizService;
import com.hyundai.softeer.backend.domain.user.entity.User;
import com.hyundai.softeer.backend.global.dto.BaseResponse;
import com.hyundai.softeer.backend.global.exception.ApiErrorResponse;
import com.hyundai.softeer.backend.global.jwt.CurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class QuizController {

    private final QuizService quizService;

    @Value("${properties.event-id}")
    private Long eventId;

    @Operation(summary = "퀴즈 문제 페이지 정보", description = """
            퀴즈 문제를 반환해주는 api
            - i번 퀴즈에 대한 정보를 응답합니다.
                
            ## 응답
            - 쿼리 파라미터의 타입이 잘못되었거나 존재하지 않으면 `400`이 반환됩니다.
            - 요청 받은 퀴즈가 이벤트 기간 중이 아니라면 `400`이 반환됩니다.
                        
            - 로그인 되지 않은 유저라면 `401`이 반환됩니다.
                        
            - 현재 진행할 예정이거나 진행중인 퀴즈가 없으면 `404`가 반환됩니다.
            """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상 반환 시", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "파라미터가 유효x, 이벤트 기간x", content = {@Content(schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject("{\"message\":\"존재하지 않는 이벤트 정보입니다.\",\"status\":400}"))}),
            @ApiResponse(responseCode = "401", description = "로그인x", content = {@Content(schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject("{\"message\":\"로그인이 되지 않았습니다.\",\"status\":401}"))}),
            @ApiResponse(responseCode = "404", description = "퀴즈x", content = {@Content(schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject("{\"message\":\"퀴즈 이벤트가 존재하지 않습니다.\",\"status\":404}"))})
    })
    @GetMapping("/api/v1/firstcome/quiz/info")
    public BaseResponse<QuizResponseDto> getQuiz(
            @ModelAttribute @Valid QuizRequest quizRequest
    ) {
        QuizResponseDto quizResponse = quizService.getQuiz(quizRequest);

        return new BaseResponse<>(quizResponse);
    }

    @Operation(summary = "퀴즈 랜딩 페이지 정보", description = """
            # 퀴즈 랜딩 페이지 정보를 반환해주는 api
                
            - 현재 이벤트에서 가장 가까운 퀴즈 문제를 반환합니다.
                - 퀴즈 이벤트 시작 전 (남은 시간이 존재합니다.)
                - 퀴즈 이벤트 중 (남은 시간 0을 반환합니다.)
                
            ## 응답
                
            - 쿼리 파라미터의 타입이 지켜지지 않거나 존재하지 않으면 예외가 발생합니다.
            - 현재 이벤트가 존재하지 않거나 퀴즈 문제가 존재하지 않으면 예외가 발생합니다.
                
            """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상 반환 시", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "GET 요청의 query parameter가 숫자가 아니거나 존재하지 않을 때", content = {@Content(schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject("{\"message\":\"존재하지 않는 이벤트 정보입니다.\",\"status\":400}"))}),
            @ApiResponse(responseCode = "404", description = "해당하는 이벤트나 현재 진행 중인 퀴즈 이벤트가 존재하지 않을 경우", content = {@Content(schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject("{\"message\":\"퀴즈 이벤트가 존재하지 않습니다.\",\"status\":404}"))})
    })
    @GetMapping("/api/v1/firstcome/quiz")
    public BaseResponse<QuizLandResponseDto> getQuizLandingPage() {
        QuizLandResponseDto getQuizResponseDto = quizService.getQuizLand(eventId);
        return new BaseResponse<>(getQuizResponseDto);
    }

    @Operation(summary = "퀴즈 제출을 채점하는 api", description = """
            # 퀴즈 제출을 체점하는 api
                
            - 퀴즈의 답변을 보내면 그에 맞는 응답을 보냅니다.
                
            ## 응답
            1. 정답이 틀린 경우
            2. 정답은 맞았으나 선착순에 들지 못한 경우.
            3. 정답도 맞고 선착순에도 든 경우.
                
            - request body의 객체의 타입이 맞지 않거나 없는 경우 예외가 발생합니다.
            """)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "퀴즈 채점이 정상적으로 처리되었을 때",
                    useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "쿼리 파라미터를 잘못 보냈을 때", content = {@Content(schema = @Schema(implementation = ApiErrorResponse.class))}),
    })
    @PostMapping("/api/v1/firstcome/quiz")
    public BaseResponse<QuizSubmitResponseDto> quizSubmit(
            @RequestBody @Validated QuizSubmitRequest quizSubmitRequest,
            @Parameter(hidden = true) @CurrentUser User user
    ) {
        QuizSubmitResponseDto quizSubmitResponseDto = quizService.quizSubmit(quizSubmitRequest, user);

        return BaseResponse.<QuizSubmitResponseDto>builder()
                .data(quizSubmitResponseDto)
                .status(HttpStatus.CREATED.value())
                .message(HttpStatus.CREATED.getReasonPhrase())
                .build();

    }
}
