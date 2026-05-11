package com.payflow.application.usecase;

import com.payflow.api.dto.CreateUserRequest;
import com.payflow.application.port.out.UserRepositoryPort;
import com.payflow.domain.exception.DuplicateUserException;
import com.payflow.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateUserUseCase {

    private final UserRepositoryPort userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User execute(CreateUserRequest request) {
        if (userRepository.existsByCpf(request.cpf())) {
            throw new DuplicateUserException("CPF", request.cpf());
        }
        if (userRepository.existsByEmail(request.email())) {
            throw new DuplicateUserException("email", request.email());
        }

        User user = User.builder()
                .fullName(request.fullName())
                .cpf(request.cpf())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .type(request.type())
                .build();

        return userRepository.save(user);
    }
}
