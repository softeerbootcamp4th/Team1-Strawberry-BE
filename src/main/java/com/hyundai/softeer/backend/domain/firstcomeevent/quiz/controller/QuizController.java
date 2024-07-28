package com.hyundai.softeer.backend.domain.firstcomeevent.quiz.controller;

import com.hyundai.softeer.backend.domain.firstcomeevent.quiz.service.QuizService;
import com.hyundai.softeer.backend.domain.firstcomeevent.quiz.dto.GetQuizRequest;
import com.hyundai.softeer.backend.domain.firstcomeevent.quiz.dto.GetQuizResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class QuizController {

    private final QuizService quizService;

    @GetMapping("/api/v1/quiz")
    public GetQuizResponseDto getQuiz(
            @RequestParam("eventId") Long eventId,
            @RequestParam("problemNumber") Integer problemNumber
            ) {
        return quizService.getQuiz(eventId, problemNumber);
    }

    @Operation(summary = "퀴즈 랜딩 페이지 정보", description = "퀴즈 랜딩 페이 정보를 반환해주는 api")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상 반환 시", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = GetQuizResponseDto.class))}),
            @ApiResponse(responseCode = "400", description = "GET 요청의 query parameter가 숫자가 아니거나 존재하지 않을 때"),
            @ApiResponse(responseCode = "204", description = "해당하는 이벤트나 현재 진행 중인 퀴즈 이벤트가 존재하지 않을 경우"),
    })
    @GetMapping("/api/v1/quiz/land")
    public ResponseEntity<GetQuizResponseDto> getQuizLandingPage(
            @RequestParam("eventId") Long eventId
    ) {
        GetQuizResponseDto getQuizResponseDto = quizService.getQuizLand(eventId);
        return ResponseEntity.ok(getQuizResponseDto);
    }
}
