package com.hyundai.softeer.backend.global.exception;

public class UsernameNotFoundException extends RuntimeException {
    public UsernameNotFoundException(String email) {
        super(email + " not found");
    }
}
