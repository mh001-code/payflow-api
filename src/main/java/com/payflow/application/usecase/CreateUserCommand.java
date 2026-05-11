package com.payflow.application.usecase;

import com.payflow.domain.model.UserType;

public record CreateUserCommand(
        String fullName,
        String cpf,
        String email,
        String password,
        UserType type
) {
}
