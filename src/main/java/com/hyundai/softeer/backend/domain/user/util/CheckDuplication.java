package com.hyundai.softeer.backend.domain.user.util;

@FunctionalInterface
public interface CheckDuplication {
    boolean check(String data);
}
