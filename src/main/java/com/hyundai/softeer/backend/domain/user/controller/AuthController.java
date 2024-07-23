package com.hyundai.softeer.backend.domain.user.controller;

import com.hyundai.softeer.backend.domain.user.dto.LoginResponseDto;
import com.hyundai.softeer.backend.domain.user.service.OAuthLoginService;
import com.hyundai.softeer.backend.global.jwt.provider.kakao.KakaoLoginParams;
import com.hyundai.softeer.backend.global.jwt.provider.naver.NaverLoginParams;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/oauth")
public class AuthController {
    private final OAuthLoginService oAuthLoginService;

    @PostMapping("/kakao")
    public ResponseEntity<LoginResponseDto> loginKakao(@RequestBody KakaoLoginParams params) {
        return ResponseEntity.ok(oAuthLoginService.login(params));
    }

    @PostMapping("/naver")
    public ResponseEntity<LoginResponseDto> loginNaver(@RequestBody NaverLoginParams params) {
        return ResponseEntity.ok(oAuthLoginService.login(params));
    }
}
