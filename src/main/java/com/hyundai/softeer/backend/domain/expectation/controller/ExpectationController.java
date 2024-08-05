package com.hyundai.softeer.backend.domain.expectation.controller;

import com.hyundai.softeer.backend.domain.expectation.dto.ExpectationsRequest;
import com.hyundai.softeer.backend.domain.expectation.dto.ExpectationsResponseDto;
import com.hyundai.softeer.backend.domain.expectation.dto.ExpectationPageRequest;
import com.hyundai.softeer.backend.domain.expectation.dto.ExpectationPageResponseDto;
import com.hyundai.softeer.backend.domain.expectation.service.ExpectationService;
import com.hyundai.softeer.backend.global.dto.BaseResponse;
import jakarta.validation.constraints.Null;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ExpectationController {

    private final ExpectationService expectationService;

    @GetMapping("/api/v1/expectation/land")
    public BaseResponse<ExpectationPageResponseDto> expectationPageApi(
            @ModelAttribute ExpectationPageRequest expectationRequest
    ) {
        ExpectationPageResponseDto expectationPage = expectationService.getExpectationPage(expectationRequest);

        return new BaseResponse<>(expectationPage);
    }

    @GetMapping("/api/v1/expectation/page")
    public BaseResponse<ExpectationsResponseDto> expectationsApi(
            @ModelAttribute ExpectationsRequest expectationsRequest
    ) {
        ExpectationsResponseDto expectations = expectationService.getExpectations(expectationsRequest);
        return new BaseResponse<>(expectations);
    }
}
