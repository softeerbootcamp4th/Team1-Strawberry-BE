package com.hyundai.softeer.backend.domain.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hyundai.softeer.backend.domain.user.dto.CallBackRequest;
import com.hyundai.softeer.backend.domain.user.dto.HyundaiUserInfo;
import com.hyundai.softeer.backend.domain.user.dto.LoginResponseDto;
import com.hyundai.softeer.backend.domain.user.dto.UserInfoDto;
import com.hyundai.softeer.backend.domain.user.entity.User;
import com.hyundai.softeer.backend.domain.user.exception.AlreadyExistOtherOAuthProviderException;
import com.hyundai.softeer.backend.domain.user.exception.DuplicateUserException;
import com.hyundai.softeer.backend.domain.user.repository.UserRepository;
import com.hyundai.softeer.backend.global.jwt.OAuthProvider;
import com.hyundai.softeer.backend.global.jwt.provider.HyundaiTokens;
import com.hyundai.softeer.backend.global.jwt.provider.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class HyundaiOauthService {

    @Value("${oauth.hyundai.client-id}")
    private String clientId;
    @Value("${oauth.hyundai.client-secret}")
    private String client_secret;
    @Value("${oauth.hyundai.state}")
    private String state;
    @Value("${oauth.hyundai.url.redirect}")
    private String redirectUri;
    @Value("${oauth.hyundai.url.auth}")
    private String authorizationUri;

    private final RestTemplate restTemplate;
    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;


    public String getHyundaiUrl() {
        return UriComponentsBuilder.fromUriString(authorizationUri + "/api/v1/user/oauth2/authorize")
                .queryParam("response_type", "code")
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("state", state)
                .build().toUriString();
    }


    public LoginResponseDto callback(CallBackRequest callBackRequest) throws JsonProcessingException {
        String code = callBackRequest.getCode();
        String state = callBackRequest.getState();

        String hyundaiAccessToken = requestAccessToken(code, state);
        HyundaiUserInfo userInfo = getHyundaiUserInfo(hyundaiAccessToken);
        List<User> user = userRepository.findByEmailOrPhoneNumber(userInfo.getEmail(), userInfo.getMobileNum());

        // 등록된 회원이라면 로그인처리
        if (!user.isEmpty()) {
            if (user.size() > 1) {
                throw new DuplicateUserException();
            }
            if (user.get(0).getOAuthProvider() != OAuthProvider.HYUNDAI) {
                throw new AlreadyExistOtherOAuthProviderException();
            }

            return LoginResponseDto.builder()
                    .user(UserInfoDto.fromEntity(user.get(0)))
                    .token(tokenProvider.createJwt(Map.of("email", userInfo.getEmail())))
                    .build();
        }

        // 등록되지 않은 회원이라면 회원가입 처리
        User newUser = User.builder()
                .email(userInfo.getEmail())
                .name(userInfo.getName())
                .phoneNumber(userInfo.getMobileNum())
                .birthDate(userInfo.getBirthdate())
                .oAuthProvider(OAuthProvider.HYUNDAI)
                .build();

        User savedUser = userRepository.save(newUser);

        return LoginResponseDto.builder()
                .user(UserInfoDto.fromEntity(savedUser))
                .token(tokenProvider.createJwt(Map.of("email", userInfo.getEmail())))
                .build();

    }

    public String requestAccessToken(String code, String state) {
        String url = authorizationUri + "/api/v1/user/oauth2/token";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String auth = clientId + ":" + client_secret;
        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.UTF_8));
        String authHeader = "Basic " + new String(encodedAuth, StandardCharsets.UTF_8);

        httpHeaders.set("Authorization", authHeader);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("redirect_uri", redirectUri);
        body.add("code", code);

        HttpEntity<?> request = new HttpEntity<>(body, httpHeaders);

        HyundaiTokens response = restTemplate.postForObject(url, request, HyundaiTokens.class);

        assert response != null;
        return response.getAccessToken();
    }

    private HyundaiUserInfo getHyundaiUserInfo(String hyundaiAccessToken) throws JsonProcessingException {
        URI uri = UriComponentsBuilder
                .fromUriString(authorizationUri + "/api/v1/user/profile")
                .build()
                .toUri();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "Bearer " + hyundaiAccessToken);

        HttpEntity<String> requestEntity = new HttpEntity<>(httpHeaders);

        ResponseEntity<String> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, requestEntity, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(responseEntity.getBody());

        HyundaiUserInfo hyundaiUserInfo = HyundaiUserInfo.builder()
                .name(rootNode.path("name").asText())
                .email(rootNode.path("email").asText())
                .birthdate(formatBirthdate(rootNode.path("birthdate").asText()))
                .mobileNum(formatMobileNumber(rootNode.path("mobileNum").asText()))
                .build();

        return hyundaiUserInfo;
    }

    private String formatMobileNumber(String mobileNum) {
        // +82 제거 및 형식 변환
        String localNum = mobileNum.substring(3); // +82 제거
        return "010-" + localNum.substring(2, 6) + "-" + localNum.substring(6);
    }

    private LocalDate formatBirthdate(String birthdate) {
        // 생년월일 형식 변환 (YYYYMMDD)
        DateTimeFormatter originalFormat = DateTimeFormatter.ofPattern("yyyyMMdd");
        return LocalDate.parse(birthdate, originalFormat);
    }
}
