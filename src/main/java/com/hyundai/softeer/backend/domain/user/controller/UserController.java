package com.hyundai.softeer.backend.domain.user.controller;

import com.hyundai.softeer.backend.domain.user.dto.UserDetailDto;
import com.hyundai.softeer.backend.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;

    @GetMapping("list")
    @Tag(name = "Admin")
    public Page<UserDetailDto> getUserList(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        return userService.getUsers(page, size);
    }
}
