package com.hyundai.softeer.backend.global.session.infra;

import com.hyundai.softeer.backend.global.session.domain.SessionManager;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Component
public class LocalStorageManager implements SessionManager {
    private final Map<String, Object> storage = new ConcurrentHashMap<>();

    @Override
    public void setAttribute(@NotNull String key, @Nullable Object value) {
        storage.put(key, value);
    }

    @Override
    public Object getAttribute(@NotNull String key) {
        return storage.get(key);
    }

    @Override
    public void removeAttribute(@NotNull String key) {
        storage.remove(key);
    }

    @Override
    public void invalidate() {
        storage.clear();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return storage;
    }

}
