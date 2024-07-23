package com.hyundai.softeer.backend.global.authentication.domain.oauth;

public interface OAuthInfoResponse {
    String getEmail();
    String getName();
    OAuthProvider getOAuthProvider();
}
