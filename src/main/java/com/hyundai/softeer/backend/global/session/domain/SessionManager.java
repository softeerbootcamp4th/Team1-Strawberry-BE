package com.hyundai.softeer.backend.global.session.domain;

import java.util.Map;

public interface SessionManager {

    void setAttribute(String key, Object value);

    Object getAttribute(String key);

    void removeAttribute(String key);

    void invalidate();

    Map<String, Object> getAttributes();
}
