package com.hyundai.softeer.backend.domain.user.service;

import com.hyundai.softeer.backend.domain.user.entity.User;
import com.hyundai.softeer.backend.domain.user.repository.UserRepository;
import com.hyundai.softeer.backend.global.authentication.domain.TokenDto;
import com.hyundai.softeer.backend.global.authentication.domain.oauth.OAuthInfoResponse;
import com.hyundai.softeer.backend.global.authentication.domain.oauth.OAuthLoginParams;
import com.hyundai.softeer.backend.global.authentication.domain.oauth.RequestOAuthInfoService;
import com.hyundai.softeer.backend.global.authentication.infra.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class OAuthLoginService {
    private final UserRepository userRepository;
    private final TokenProvider jwtTokenProvider;
    private final RequestOAuthInfoService requestOAuthInfoService;

    public TokenDto login(OAuthLoginParams params) {
        OAuthInfoResponse oAuthInfoResponse = requestOAuthInfoService.request(params);
        Long userId = findOrCreateMember(oAuthInfoResponse);
        User user = userRepository.findById(userId).get();
        Map<String, Object> claims = Map.of("email", user.getEmail());
        return jwtTokenProvider.createJwt(claims);
    }

    private Long findOrCreateMember(OAuthInfoResponse oAuthInfoResponse) {
        return userRepository.findByEmail(oAuthInfoResponse.getEmail())
                .map(User::getId)
                .orElseGet(() -> newMember(oAuthInfoResponse));
    }

    private Long newMember(OAuthInfoResponse oAuthInfoResponse) {
        User member = User.builder()
                .email(oAuthInfoResponse.getEmail())
                .name(oAuthInfoResponse.getName())
                .oAuthProvider(oAuthInfoResponse.getOAuthProvider())
                .build();

        return userRepository.save(member).getId();
    }
}
