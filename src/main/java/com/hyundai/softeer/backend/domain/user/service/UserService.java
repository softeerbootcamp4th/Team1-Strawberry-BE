package com.hyundai.softeer.backend.domain.user.service;

import com.hyundai.softeer.backend.domain.user.dto.UserDetailDto;
import com.hyundai.softeer.backend.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Page<UserDetailDto> getUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        return userRepository.findAll(pageable)
                .map(user -> UserDetailDto.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .email(user.getEmail())
                        .phoneNumber(user.getPhoneNumber())
                        .birthDate(user.getBirthDate())
                        .oAuthProvider(String.valueOf(user.getOAuthProvider()))
                        .build());

    }
}
