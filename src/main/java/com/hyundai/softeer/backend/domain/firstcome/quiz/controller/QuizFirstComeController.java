package com.hyundai.softeer.backend.domain.firstcome.quiz.controller;

import com.hyundai.softeer.backend.domain.firstcome.dto.EnqueueDto;
import com.hyundai.softeer.backend.domain.firstcome.dto.QueueRequest;
import com.hyundai.softeer.backend.domain.firstcome.dto.WaitingQueueStatusDto;
import com.hyundai.softeer.backend.domain.firstcome.quiz.dto.*;
import com.hyundai.softeer.backend.domain.firstcome.quiz.exception.QuizRegisterForbiddenException;
import com.hyundai.softeer.backend.domain.firstcome.quiz.service.QuizFirstComeService;
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
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Quiz First Come")
@RequestMapping("/api/v1/firstcome/quiz")
public class QuizFirstComeController {
    private final QuizFirstComeService quizFirstComeService;


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
    @GetMapping("/info")
    @SecurityRequirement(name = "access-token")
    public BaseResponse<QuizFirstComeResponseDto> getQuiz(
            @Valid QuizFirstComeRequest quizFirstComeRequest
    ) {
        QuizFirstComeResponseDto quizResponse = quizFirstComeService.getQuiz(quizFirstComeRequest);

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
    @GetMapping("/land")
    public BaseResponse<QuizFirstComeLandResponseDto> getQuizLandingPage() {
        QuizFirstComeLandResponseDto getQuizResponseDto = quizFirstComeService.getQuizLand(eventId);
        return new BaseResponse<>(getQuizResponseDto);
    }

    @Operation(summary = "퀴즈 제출을 채점하는 api", description = """
            # 퀴즈 제출을 체점하는 api
                
            - 퀴즈의 답변을 보내면 그에 맞는 응답을 보냅니다.
                
            ## 응답
            1. 정답이 틀린 경우
            2. 정답은 맞았으나 선착순에 들지 못한 경우.
            3. 정답도 맞고 선착순에도 든 경우.
            4. 정답도 맞고 선착순에도 들었지만 이미 참가한 경우.
                
            - request body의 객체의 타입이 맞지 않거나 없는 경우 예외가 발생합니다.
            """)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "퀴즈 채점이 정상적으로 처리되었을 때",
                    useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "쿼리 파라미터를 잘못 보냈을 때", content = {@Content(schema = @Schema(implementation = ApiErrorResponse.class))}),
    })
    @PostMapping("")
    @SecurityRequirement(name = "access-token")
    public BaseResponse<QuizFirstComeSubmitResponseDto> quizSubmit(
            @RequestBody @Validated QuizFirstComeSubmitRequest quizFirstComeSubmitRequest,
            @Parameter(hidden = true) @CurrentUser User user
    ) {
        QuizFirstComeSubmitResponseDto quizFirstComeSubmitResponseDto = quizFirstComeService.quizSubmit(quizFirstComeSubmitRequest, user);

        return BaseResponse.<QuizFirstComeSubmitResponseDto>builder()
                .data(quizFirstComeSubmitResponseDto)
                .status(HttpStatus.CREATED.value())
                .message(HttpStatus.CREATED.getReasonPhrase())
                .build();

    }

    @GetMapping("/list")
    @Operation(summary = "admin / 퀴즈 정보를 반환하는 api", description = """
            # 퀴즈 정보를 반환한다. 
                        
            - 퀴즈 정보를 3개 반환합니다.
                
            ## 응답
            1. 응답에 성공 했을 때 `200` 응답
            2. 이벤트가 존재하지 않는 경우 `404` 응답
            3. 쿼리 파라미터가 없거나 유효하지 않을 때 `400` 응답
            """)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "퀴즈 정보를 정상적으로 반환했을 때",
                    useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "쿼리 파라미터가 없거나 유효하지 않을 때", content = {@Content(schema = @Schema(implementation = ApiErrorResponse.class))}),
            @ApiResponse(responseCode = "404", description = "이벤트가 존재하지 않을 때", content = {@Content(schema = @Schema(implementation = ApiErrorResponse.class))}),
    })
    @Tag(name = "Admin")
    public BaseResponse<List<QuizFirstComeInfoResponseDto>> getQuizInfos(
            @Validated QuizFirstComeInfoRequest quizFirstComeInfoRequest
    ) {
        return new BaseResponse<>(quizFirstComeService.getQuizInfos(quizFirstComeInfoRequest));
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "admin / 퀴즈를 등록하는 api", description = """
            # 퀴즈를 등록하는 api
                        
            - 퀴즈 3개를 동시에 등록합니다.
                
            ## 응답
            1. 응답에 성공 했을 때 `200` 응답
            2. 쿼리 파라미터가 없거나 유효하지 않을 때 `400` 응답
            3. 등록하려는 퀴즈 정보가 3개가 아니라면 `400` 응답
            4. 이벤트가 존재하지 않는 경우 `404` 응답
                        
            ## 주의
            - 퀴즈는 반드시 3개가 들어와야 합니다.
            """)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "퀴즈 정보를 정상적으로 반환했을 때",
                    useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "쿼리 파라미터가 없거나 유효하지 않을 때", content = {@Content(schema = @Schema(implementation = ApiErrorResponse.class))}),
            @ApiResponse(responseCode = "404", description = "이벤트가 존재하지 않을 때", content = {@Content(schema = @Schema(implementation = ApiErrorResponse.class))}),
    })
    @Tag(name = "Admin")
    public BaseResponse<Void> registerQuiz(
            @Validated @RequestBody List<QuizFirstComeRegisterRequest> quizFirstComeRegisterRequests
    ) {
        if (quizFirstComeRegisterRequests.size() != 3) {
            throw new QuizRegisterForbiddenException();
        }

        quizFirstComeService.registerQuiz(quizFirstComeRegisterRequests);

        return new BaseResponse<>(201, "퀴즈가 등록되었습니다.", null);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "admin / 퀴즈를 삭제하는 api", description = """
            # 퀴즈를 삭제하는 api
                        
            - 한 이벤트의 퀴즈 이벤트를 삭제합니다.
            - 퀴즈 이벤트 3개를 모두 삭제하는 api 입니다.
                
            ## 응답
            1. 퀴즈 이벤트 삭제에 성공했을 때 `204` 응답
            2. 쿼리 파라미터가 없거나 유효하지 않을 때 `400` 응답
                        
            """)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "퀴즈 이벤트가 정상적으로 삭제되었을 때",
                    useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "쿼리 파라미터가 없거나 유효하지 않을 때", content = {@Content(schema = @Schema(implementation = ApiErrorResponse.class))}),
    })
    @Tag(name = "Admin")
    public BaseResponse<Void> deleteQuizEvent(
            @Validated @RequestBody QuizFirstComeDeleteRequest quizFirstComeDeleteRequest
    ) {
        quizFirstComeService.deleteQuizByEvent(quizFirstComeDeleteRequest);

        return new BaseResponse<>(200, "퀴즈 이벤트 삭제가 성공적으로 실행되었습니다.", null);
    }

    @PostMapping("/enqueue")
    @SecurityRequirement(name = "access-token")
    public EnqueueDto enqueueQuiz(
            @RequestBody QueueRequest queueRequest,
            @Parameter(hidden = true) @CurrentUser User authenticatedUser
    ) {
        return quizFirstComeService.enqueueQuiz(authenticatedUser, queueRequest);
    }

    @GetMapping("/status")
    @SecurityRequirement(name = "access-token")
    public WaitingQueueStatusDto getQueueStatus(
            QueueRequest queueRequest
    ) {
        return quizFirstComeService.getQueueStatus(queueRequest);
    }
}
