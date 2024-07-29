package com.hyundai.softeer.backend.domain.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hyundai.softeer.backend.domain.user.dto.LoginResponseDto;
import com.hyundai.softeer.backend.domain.user.dto.NaverUserInfo;
import com.hyundai.softeer.backend.domain.user.dto.UserInfoDto;
import com.hyundai.softeer.backend.domain.user.entity.User;
import com.hyundai.softeer.backend.domain.user.repository.UserRepository;
import com.hyundai.softeer.backend.global.jwt.OAuthProvider;
import com.hyundai.softeer.backend.global.jwt.provider.NaverTokens;
import com.hyundai.softeer.backend.global.jwt.provider.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NaverOauthService {

    @Value("${oauth.naver.client-id}")
    private String clientId;
    @Value("${oauth.naver.client-secret}")
    private String secret;
    @Value("${oauth.naver.state}")
    private String state;
    @Value("${oauth.naver.url.redirect}")
    private String redirectUri;
    @Value("${oauth.naver.url.api}")
    private String apiUrl;
    @Value("${oauth.naver.url.auth}")
    private String authorizationUri;

    private final RestTemplate restTemplate;
    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;


    public String getNaverUrl() {
        return UriComponentsBuilder.fromUriString(authorizationUri + "/oauth2.0/authorize")
                .queryParam("response_type", "code")
                .queryParam("client_id", clientId)
                .queryParam("state", state)
                .queryParam("redirect_uri", redirectUri)
                .build().toUriString();
    }


    public LoginResponseDto callback(String code, String state) throws JsonProcessingException {
        String naverAccessToken = requestAccessToken(code, state);
        NaverUserInfo userInfo = getNaverUserInfo(naverAccessToken);
        Optional<User> user = userRepository.findByEmail(userInfo.getEmail());

        // 등록된 회원이라면 로그인처리
        if (user.isPresent()) {
            return LoginResponseDto.builder()
                    .user(UserInfoDto.fromEntity(user.get()))
                    .token(tokenProvider.createJwt(Map.of("email", userInfo.getEmail())))
                    .build();
        }

        // 등록되지 않은 회원이라면 회원가입 처리
        User newUser = User.builder()
                .email(userInfo.getEmail())
                .name(userInfo.getName())
                .phoneNumber(userInfo.getMobile())
                .birthDate(LocalDate.parse(userInfo.getBirthyear() + "-" + userInfo.getBirthday()))
                .oAuthProvider(OAuthProvider.NAVER)
                .build();

        User savedUser = userRepository.save(newUser);

        return LoginResponseDto.builder()
                .user(UserInfoDto.fromEntity(savedUser))
                .token(tokenProvider.createJwt(Map.of("email", userInfo.getEmail())))
                .build();

    }

    public String requestAccessToken(String code, String state) {
        String url = authorizationUri + "/oauth2.0/token";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("client_secret", secret);
        body.add("redirect_uri", redirectUri);
        body.add("code", code);
        body.add("state", state);

        HttpEntity<?> request = new HttpEntity<>(body, httpHeaders);

        NaverTokens response = restTemplate.postForObject(url, request, NaverTokens.class);

        assert response != null;
        return response.getAccessToken();
    }

    private NaverUserInfo getNaverUserInfo(String naverAccessToken) throws JsonProcessingException {
        URI uri = UriComponentsBuilder
                .fromUriString(apiUrl + "/v1/nid/me")
                .queryParam("access_token", naverAccessToken)
                .encode()
                .build()
                .toUri();

        ResponseEntity<String> responseEntity = restTemplate.getForEntity(uri, String.class);

        // 응답 본문을 JSON으로 파싱하여 NaverUserInfo로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(responseEntity.getBody());
        JsonNode responseNode = rootNode.path("response");

        // responseNode에서 직접 필요한 필드를 추출
        NaverUserInfo naverUserInfo = NaverUserInfo.builder()
                .name(responseNode.path("name").asText())
                .email(responseNode.path("email").asText())
                .birthday(responseNode.path("birthday").asText())
                .birthyear(responseNode.path("birthyear").asText())
                .mobile(responseNode.path("mobile").asText())
                .build();

        return naverUserInfo;
    }
}
