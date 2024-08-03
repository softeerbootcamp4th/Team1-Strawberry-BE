package com.hyundai.softeer.backend.domain.firstcomeevent.quiz.controller;

import com.hyundai.softeer.backend.domain.firstcomeevent.quiz.dto.QuizResponseDto;
import com.hyundai.softeer.backend.domain.firstcomeevent.quiz.dto.QuizSubmitRequest;
import com.hyundai.softeer.backend.domain.firstcomeevent.quiz.dto.QuizSubmitResponseDto;
import com.hyundai.softeer.backend.domain.firstcomeevent.quiz.service.QuizService;
import com.hyundai.softeer.backend.domain.firstcomeevent.quiz.dto.QuizLandResponseDto;
import com.hyundai.softeer.backend.domain.user.entity.User;
import com.hyundai.softeer.backend.global.jwt.CurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class QuizController {

    private final QuizService quizService;

    @Operation(summary = "퀴즈 문제 페이지 정보", description = "퀴즈 문제를 반환해주는 api")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상 반환 시", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)}, useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "쿼리 파라미터의 타입이 잘못되었거나 존재하지 않을 때", content = {@Content(schema = @Schema(implementation = Void.class))}),
            @ApiResponse(responseCode = "204", description = "해당하는 퀴즈 이벤트가 없는 경우", content = {@Content(schema = @Schema(implementation = Void.class))}),
    })
    @GetMapping("/api/v1/quiz")
    public QuizResponseDto getQuiz(
            @RequestParam("eventId") Long eventId,
            @RequestParam("problemNumber") Integer problemNumber
            ) {
        return quizService.getQuiz(eventId, problemNumber);
    }

    @Operation(summary = "퀴즈 랜딩 페이지 정보", description = "퀴즈 랜딩 페이 정보를 반환해주는 api")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상 반환 시", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)}, useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "GET 요청의 query parameter가 숫자가 아니거나 존재하지 않을 때", content = {@Content(schema = @Schema(implementation = Void.class))}),
            @ApiResponse(responseCode = "204", description = "해당하는 이벤트나 현재 진행 중인 퀴즈 이벤트가 존재하지 않을 경우", content = {@Content(schema = @Schema(implementation = Void.class))})
    })
    @GetMapping("/api/v1/quiz/land")
    public ResponseEntity<QuizLandResponseDto> getQuizLandingPage(
            @RequestParam("eventId") Long eventId
    ) {
        QuizLandResponseDto getQuizResponseDto = quizService.getQuizLand(eventId);

        return ResponseEntity.ok(getQuizResponseDto);
    }


    @Operation(summary = "퀴즈 제출을 채점하는 api", description = "퀴즈 제출을 체점하는 api")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "퀴즈 채점이 정상적으로 처리되었을 때", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)}, useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "쿼리 파라미터를 잘못 보냈을 때", content = {@Content(schema = @Schema(implementation = Void.class))}),
    })
    @PostMapping("/api/v1/quiz/submit")
    public ResponseEntity<QuizSubmitResponseDto> quizSubmit(
            @RequestBody QuizSubmitRequest quizSubmitRequest,
            @Parameter(hidden = true) @CurrentUser User user
    ) {
        QuizSubmitResponseDto quizSubmitResponseDto = quizService.quizSubmit(quizSubmitRequest, user);

        return ResponseEntity.ok(quizSubmitResponseDto);
    }
}
