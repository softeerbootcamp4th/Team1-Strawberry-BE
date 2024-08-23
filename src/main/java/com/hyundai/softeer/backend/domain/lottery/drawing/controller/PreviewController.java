package com.hyundai.softeer.backend.domain.lottery.drawing.controller;

import com.hyundai.softeer.backend.domain.eventuser.entity.EventUser;
import com.hyundai.softeer.backend.domain.lottery.drawing.dto.PreviewRequest;
import com.hyundai.softeer.backend.domain.lottery.drawing.service.PreviewService;
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
