package com.hyundai.softeer.backend.domain.user.controller;

import com.hyundai.softeer.backend.domain.user.dto.LoginResponseDto;
import com.hyundai.softeer.backend.domain.user.service.HyundaiOauthService;
import com.hyundai.softeer.backend.domain.user.service.NaverOauthService;
import com.hyundai.softeer.backend.global.dto.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/oauth2")
@Tag(name = "User")
public class AuthController {
    private final NaverOauthService naverOauthService;
    private final HyundaiOauthService hyundaiOauthService;

    @GetMapping("/naver")
    @Operation(summary = "네이버 로그인", description = """
            # 네이버 로그인
                
            - 이 API를 호출하면 네이버 로그인 페이지로 리다이렉트됩니다.
              - 이 API를 네이버 로그인 버튼의 링크로 사용합니다.
            - 네이버 로그인 페이지에서 로그인을 완료하면 콜백 URL로 리다이렉트됩니다.
            - 콜백 URL에서 네이버 인가 코드를 받아서 처리합니다.
            """)
    @ApiResponse(
            responseCode = "302",
            headers = {
                    @Header(
                            name = "Location",
                            description = """
                                    - 네이버 로그인 페이지 URL
                                    - ex) https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id=CLIENT_ID&state=STATE_STRING&redirect_uri=CALLBACK_URL
                                    """)
            },
            description = "네이버 로그인 페이지로 리다이렉트됩니다.")
    public void redirectNaver(HttpServletResponse response) throws IOException {
        response.sendRedirect(naverOauthService.getNaverUrl());
    }

    @GetMapping("/naver/callback")
    @Operation(summary = "네이버 로그인 콜백", description = """
            # 네이버 로그인 콜백

            - 네이버 로그인 페이지에서 로그인을 완료하면 네이버 인가 코드와 함께 이 API로 리다이렉트됩니다.
            - 네이버 인가 코드를 사용해 계정의 정보를 조회 및 처리합니다.
            - DB에 등록된 이메일이면 로그인 처리, 등록되지 않은 이메일이면 회원가입 페이지로 이동 합니다.
            - 로그인 시 200 코드와 함께 유저정보, 토큰을 반환합니다.
            """)
    @Parameter(name = "code", description = "네이버 인가 코드", required = true)
    @ApiResponse(
            responseCode = "200",
            description = "로그인 성공 응답을 반환합니다.",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = LoginResponseDto.class)
            ))
    public BaseResponse<LoginResponseDto> callbackNaver(@RequestParam String code, @RequestParam String state) throws IOException {
        return BaseResponse.<LoginResponseDto>builder()
                .data(naverOauthService.callback(code, state))
                .build();
    }

    @GetMapping("/hyundai")
    @Operation(summary = "현대자동차 로그인", description = """
            # 현대자동차 로그인
                
            - 이 API를 호출하면 현대자동차 로그인 페이지로 리다이렉트됩니다.
              - 이 API를 현대자동차 로그인 버튼의 링크로 사용합니다.
            - 현대자동차 로그인 페이지에서 로그인을 완료하면 콜백 URL로 리다이렉트됩니다.
            - 콜백 URL에서 현대자동차 인가 코드를 받아서 처리합니다.
            """)
    @ApiResponse(
            responseCode = "302",
            headers = {
                    @Header(
                            name = "Location",
                            description = """
                                    - 현대자동차 로그인 페이지 URL
                                    - ex) https://prd.kr-ccapi.hyundai.com/api/v1/user/oauth2/authorize?response_type=code&client_id=CLIENT_ID&state=STATE_STRING&redirect_uri=CALLBACK_URL
                                    """)
            },
            description = "현대자동차 로그인 페이지로 리다이렉트됩니다.")
    public void redirectHyundai(HttpServletResponse response) throws IOException {
        response.sendRedirect(hyundaiOauthService.getHyundaiUrl());
    }

    @GetMapping("/hyundai/callback")
    @Operation(summary = "현대자동차 로그인 콜백", description = """
            # 현대자동차 로그인 콜백

            - 현대자동차 로그인 페이지에서 로그인을 완료하면 현대자동차 인가 코드와 함께 이 API로 리다이렉트됩니다.
            - 현대자동차 인가 코드를 사용해 계정의 정보를 조회 및 처리합니다.
            - DB에 등록된 이메일이면 로그인 처리, 등록되지 않은 이메일이면 회원가입 페이지로 이동 합니다.
            - 로그인 시 200 코드와 함께 유저정보, 토큰을 반환합니다.
            """)
    @Parameter(name = "code", description = "현대자동차 인가 코드", required = true)
    @ApiResponse(
            responseCode = "200",
            description = "로그인 성공 응답을 반환합니다.",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = LoginResponseDto.class)
            ))
    public BaseResponse<LoginResponseDto> callbackHyundai(@RequestParam String code, @RequestParam String state) throws IOException {
        return BaseResponse.<LoginResponseDto>builder()
                .data(hyundaiOauthService.callback(code, state))
                .build();
    }
}
