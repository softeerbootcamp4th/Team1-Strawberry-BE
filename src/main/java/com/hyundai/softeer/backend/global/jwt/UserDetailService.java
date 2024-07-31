package com.hyundai.softeer.backend.global.jwt;

import com.hyundai.softeer.backend.domain.user.entity.User;
import com.hyundai.softeer.backend.domain.user.repository.UserRepository;
import com.hyundai.softeer.backend.global.exception.UsernameNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailService {
    private final UserRepository userRepository;

    public User loadUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));
    }
}
