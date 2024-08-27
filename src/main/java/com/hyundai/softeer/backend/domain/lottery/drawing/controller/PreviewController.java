package com.hyundai.softeer.backend.domain.lottery.drawing.controller;

import com.hyundai.softeer.backend.domain.eventuser.entity.EventUser;
import com.hyundai.softeer.backend.domain.lottery.drawing.dto.PreviewRequest;
import com.hyundai.softeer.backend.domain.lottery.drawing.service.PreviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class PreviewController {

    private final PreviewService previewService;

    @Value("${domain.url}")
    private String baseUrl;

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
    @GetMapping("/{sharedUrl}")
    public String preview(
            @PathVariable String sharedUrl,
            Model model
    ) {
        EventUser eventUser = previewService.preview(sharedUrl, model);

        model.addAttribute("resultImgUrl", eventUser.getResultImgUrl());
        model.addAttribute("name", eventUser.getUser().getName());
        model.addAttribute("apiUrl", baseUrl + "/api/v1");
        model.addAttribute("ogUrl", baseUrl + "/" + sharedUrl);
        model.addAttribute("sharedUrl", sharedUrl);

        return "preview";
    }
}
