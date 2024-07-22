package com.hyundai.softeer.backend.global.session.application;

import com.hyundai.softeer.backend.global.session.domain.SessionManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final SessionManager sessionManager;

    public void setAttribute(String key, Object value) {
        sessionManager.setAttribute(key, value);
    }

    public Object getAttribute(String key) {
        return sessionManager.getAttribute(key);
    }

    public void removeAttribute(String key) {
        sessionManager.removeAttribute(key);
    }

    public void invalidate() {
        sessionManager.invalidate();
    }

    public Map<String, Object> getAttributes() {
        return sessionManager.getAttributes();
    }

}
