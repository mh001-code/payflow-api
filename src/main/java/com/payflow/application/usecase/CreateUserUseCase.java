package com.payflow.application.usecase;

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
    public User execute(CreateUserCommand command) {
        if (userRepository.existsByCpf(command.cpf())) {
            throw new DuplicateUserException("CPF", command.cpf());
        }
        if (userRepository.existsByEmail(command.email())) {
            throw new DuplicateUserException("email", command.email());
        }

        User user = User.builder()
                .fullName(command.fullName())
                .cpf(command.cpf())
                .email(command.email())
                .password(passwordEncoder.encode(command.password()))
                .type(command.type())
                .build();

        return userRepository.save(user);
    }
}
