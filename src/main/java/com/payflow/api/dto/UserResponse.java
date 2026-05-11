package com.payflow.api.dto;

import com.payflow.domain.model.User;
import com.payflow.domain.model.UserType;

import java.time.LocalDateTime;

public record UserResponse(
        Long id,
        String fullName,
        String cpf,
        String email,
        UserType type,
        LocalDateTime createdAt
) {

    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getFullName(),
                user.getCpf(),
                user.getEmail(),
                user.getType(),
                user.getCreatedAt()
        );
    }
}
