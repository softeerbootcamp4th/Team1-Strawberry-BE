package com.hyundai.softeer.backend.domain.lottery.drawing.controller;

import com.hyundai.softeer.backend.domain.eventuser.entity.EventUser;
import com.hyundai.softeer.backend.domain.lottery.drawing.dto.PreviewRequest;
import com.hyundai.softeer.backend.domain.lottery.drawing.service.PreviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1/lottery/drawing")
@RequiredArgsConstructor
public class PreviewController {

    private final PreviewService previewService;

    @Tag(name = "Drawing Lottery")
    @Operation(summary = "미리 보기용 html 반환", description = """
            # open graph html 반환
            
            - 이미지 url과 점수, 이름이 담긴 open graph html이 반환됩니다.
             
            ## 응답
                        
            - 렌더링 성공 시 `200`이 반환됩니다.
             
            """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "html 렌더링 성공 시", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "이벤트를 참여하지 않은 sharedUrl이 들어올 경우", useReturnTypeSchema = true)
    })
    @GetMapping("/preview")
    public String preview(
            @Validated PreviewRequest previewRequest,
            Model model
    ) {
        EventUser eventUser = previewService.preview(previewRequest);

        model.addAttribute("resultImgUrl", eventUser.getResultImgUrl());
        model.addAttribute("name", eventUser.getUser().getName());
        model.addAttribute("score", eventUser.getGameScore());

        return "preview";
    }
}
