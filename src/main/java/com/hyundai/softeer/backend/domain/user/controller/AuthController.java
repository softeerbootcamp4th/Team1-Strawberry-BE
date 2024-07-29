package com.hyundai.softeer.backend.domain.user.controller;


import com.hyundai.softeer.backend.domain.user.service.HyundaiOauthService;
import com.hyundai.softeer.backend.domain.user.service.NaverOauthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/oauth2")
public class AuthController {
    private final NaverOauthService naverOauthService;
    private final HyundaiOauthService hyundaiOauthService;

    @GetMapping("/naver")
    public void redirectNaver(HttpServletResponse response) throws IOException {
        response.sendRedirect(naverOauthService.getNaverUrl());
    }

    @GetMapping("/naver/callback")
    public ResponseEntity<?> callbackNaver(@RequestParam String code, @RequestParam String state) throws IOException {
        return naverOauthService.callback(code, state);
    }

    @GetMapping("/hyundai")
    public void redirectHyundai(HttpServletResponse response) throws IOException {
        response.sendRedirect(hyundaiOauthService.getHyundaiUrl());
    }

    @GetMapping("/hyundai/callback")
    public ResponseEntity<?> callbackHyundai(@RequestParam String code, @RequestParam String state) throws IOException {
        return hyundaiOauthService.callback(code, state);
    }


}
