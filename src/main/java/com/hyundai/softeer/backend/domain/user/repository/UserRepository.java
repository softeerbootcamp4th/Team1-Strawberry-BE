package com.hyundai.softeer.backend.domain.user.repository;

import com.hyundai.softeer.backend.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByPhoneNumber(String phoneNumber);

    List<User> findByEmailOrPhoneNumber(String email, String phoneNumber);
}
