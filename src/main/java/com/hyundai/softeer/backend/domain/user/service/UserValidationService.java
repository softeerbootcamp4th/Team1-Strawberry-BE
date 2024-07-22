package com.hyundai.softeer.backend.domain.user.service;

import com.hyundai.softeer.backend.domain.user.dto.CreateGoogleUserRequest;
import com.hyundai.softeer.backend.domain.user.dto.CreateUserRequest;
import com.hyundai.softeer.backend.domain.user.excepiton.DuplicateDataException;
import com.hyundai.softeer.backend.domain.user.repository.UserRepository;
import com.hyundai.softeer.backend.domain.user.util.CheckDuplication;
import com.hyundai.softeer.backend.domain.user.util.FieldName;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class UserValidationService {

    private final Map<FieldName, CheckDuplication> checkers;

    public UserValidationService(UserRepository userRepository) {
        checkers = Map.of(
                FieldName.Nickname, userRepository::existsByNickname,
                FieldName.Phone, userRepository::existsByPhone,
                FieldName.Email, userRepository::existsByEmail
        );
    }

    public void checkDuplicationData(FieldName type, String data) {
        if (checkers.containsKey(type) && checkers.get(type).check(data)) {
            throw new DuplicateDataException(data);
        }
    }

    public void checkUniqueUser(CreateUserRequest dto) {
        if (checkers.get(FieldName.Email).check(dto.getEmail())) {
            throw new DuplicateDataException(dto.getEmail());
        }
        if (checkers.get(FieldName.Nickname).check(dto.getNickname())) {
            throw new DuplicateDataException(dto.getNickname());
        }
        if (dto.getPhone() != null && checkers.get(FieldName.Phone).check(dto.getPhone())) {
            throw new DuplicateDataException(dto.getPhone());
        }
    }

    public void checkUniqueUser(CreateGoogleUserRequest dto) {
        if (checkers.get(FieldName.Nickname).check(dto.getNickname())) {
            throw new DuplicateDataException(dto.getNickname());
        }
        if (dto.getPhone() != null && checkers.get(FieldName.Phone).check(dto.getPhone())) {
            throw new DuplicateDataException(dto.getPhone());
        }
    }

    public void checkEmailDupl(String email) {
        if (checkers.get(FieldName.Email).check(email)) {
            throw new DuplicateDataException(email);
        }
    }


}
