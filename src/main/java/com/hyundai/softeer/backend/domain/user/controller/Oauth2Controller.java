package com.hyundai.softeer.backend.domain.user.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.hyundai.softeer.backend.domain.user.dto.CreateGoogleUserRequest;
import com.hyundai.softeer.backend.domain.user.dto.GoogleUserInfoDto;
import com.hyundai.softeer.backend.domain.user.dto.LoginResponseDto;
import com.hyundai.softeer.backend.domain.user.dto.UserInfoDto;
import com.hyundai.softeer.backend.domain.user.service.GoogleOauthService;
import com.hyundai.softeer.backend.global.document.ValidationErrorResponse;
import com.hyundai.softeer.backend.global.exception.ApiErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/v1/oauth2")
@RequiredArgsConstructor
@Tag(name = "Login", description = "로그인 관련 API")
public class Oauth2Controller {

    private final GoogleOauthService googleOauthService;

    @GetMapping("/google")
    @Operation(summary = "구글 로그인", description = """
            # 구글 로그인
                        
            - 이 API를 호출하면 구글 로그인 페이지로 리다이렉트됩니다.
              - 이 API를 구글 로그인 버튼의 링크로 사용합니다. 
            - 구글 로그인 페이지에서 로그인을 완료하면 콜백 URL로 리다이렉트됩니다.
            - 콜백 URL에서 구글 인가 코드를 받아서 처리합니다.
                        
            """)

    @ApiResponse(
            responseCode = "302",
            headers = {
                    @Header(
                            name = "Location",
                            description = """
                                    - 구글 로그인 페이지 URL
                                    - ex) https://accounts.google.com/o/oauth2/v2/auth?client_id={client_id}&redirect_uri={redirect_uri}&response_type=code&scope=openid profile email
                                    """)
            },
            description = "구글 로그인 페이지로 리다이렉트됩니다.")
    public void redirectGoogle(HttpServletResponse response) throws IOException {
        response.sendRedirect(googleOauthService.getGoogleLoginUrl());
    }

    @GetMapping("/callback")
    @Operation(summary = "구글 로그인 콜백", description = """
            # 구글 로그인 콜백

            - 구글 로그인 페이지에서 로그인을 완료하면 구글 인가 코드와 함께 이 API로 리다이렉트됩니다.
            - 구글 인가 코드를 사용해 계정의 정보를 조회 및 처리합니다.
            - DB에 등록된 이메일이면 로그인 처리, 등록되지 않은 이메일이면 회원가입 페이지로 이동 합니다.
            - 로그인 시 200 코드와 함께 유저정보, 토큰을 반환합니다.
            - 회원가입 시 301 코드와 함께 회원 정보를 반환합니다.
                - 서버에서 생성한 uuid를 추가로 전달하며, 이 구글 계정 회원가입 요청 시 이 uuid이 포함되어야합니다.
              
            회원정보 반환 예시:
                        
            ```json
            {
              "id": "114210435473335230303",
              "name": "Mat thew",
              "given_name": "Mat",
              "family_name": "thew",
              "locale": "ko",
              "email": "swyg46@gmail.com",
              "verified_email": true,
              "picture": "https://lh3.googleusercontent.com/a/ACg8ocKrQm8D09Njibbo_vgeOQZt_oBdu63c6qOgxsWZ6QOJdcflxA=s96-c",
              // 구글계정 회원가입 요청 시 필요한 uuid
              "uuid": "58effd3e-431b-4ec4-8240-a5636e3a47f7"
            }
            """)
    @Parameter(name = "code", description = "구글 인가 코드", required = true)
    @ApiResponse(
            responseCode = "200",
            description = "등록된 구글 계정이라면 로그인 성공 응답을 반환합니다.",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = LoginResponseDto.class)
            ))
    @ApiResponse(
            responseCode = "301",
            description = "등록되지 않은 구글계정이라면 구글 계정 정보와 함께 회원가입 페이지로 리다이렉트합니다.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = GoogleUserInfoDto.class)))
    public ResponseEntity<?> callback(@RequestParam String code) throws JsonProcessingException {
        String decodedCode = URLDecoder.decode(code, StandardCharsets.UTF_8);
        return googleOauthService.callback(decodedCode);
    }

    @PostMapping("/google/signup")
    @Operation(summary = "구글 계정으로 회원 가입", description = """
            # 구글 계정 회원가입
                        
            구글 계정으로 회원가입을 수행합니다.. 회원 가입 시 닉네임, 이름, 전화번호, 국적, 성별, 비밀번호, 비밀번호 확인을 입력합니다.
            각 필드의 제약 조건은 다음과 같습니다.

            | 필드명 | 설명 | 제약조건 | 중복확인 | 예시 |
            |--------|------|----------|----------|------|
            | nickname | 다른 사용자들에게 보이는 닉네임 | 4~20자 | Y | nickname01 |
            | name | 사용자의 이름 | 2~20자 | N | name01 |
            | phone | 사용자의 전화번호 | '-'를 제외한 숫자, null 가능 | Y | 01012345678 |
            | birthdate | 사용자의 생년월일 | `yyyy-MM-dd` 형식의 문자열 | N | 1990-01-01 |
            | uuid | 구글 로그인 시 callback에서 발급받은 uuid | 문자열 | N | 58effd3e-431b-4ec4-8240-a5636e3a47f7 |
             
            ## 응답
                        
            - 회원 가입 성공 시 `200` 코드와 함께 회원 이메일을 문자열로 반환합니다.
            - 입력 양식에 오류가 있을 경우 `400` 에러를 반환합니다.
                - 유효하지 않은 uuid일 경우에도 `400`에러를 반환합니다.
            - 중복된 값이 있을 경우 `409` 에러를 반환합니다.
             
            """)
    @ApiResponse(
            responseCode = "200",
            description = "생성한 계정 고유 번호를 반환합니다.",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = UserInfoDto.class)))
    @ApiResponse(
            responseCode = "409",
            description = "입력 값 중 중복된 값이 있습니다.",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ApiErrorResponse.class),
                    examples = @ExampleObject(value = "{\n  \"status\": \"CONFLICT\",\n  \"message\": \"데이터 중복\"\n}")
            )
    )
    @ValidationErrorResponse
    public UserInfoDto createGoogleUser(@Valid @RequestBody CreateGoogleUserRequest dto) throws JsonProcessingException {
        return googleOauthService.createUser(dto);
    }

}
