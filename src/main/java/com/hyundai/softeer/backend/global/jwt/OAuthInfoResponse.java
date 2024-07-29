package com.hyundai.softeer.backend.global.jwt;

public interface OAuthInfoResponse {
    String getEmail();
    String getName();
    OAuthProvider getOAuthProvider();
}
