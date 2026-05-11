package com.payflow.application.usecase;

import com.payflow.application.port.out.UserRepositoryPort;
import com.payflow.domain.exception.UserNotFoundException;
import com.payflow.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FindUserUseCase {

    private final UserRepositoryPort userRepository;

    @Transactional(readOnly = true)
    public User execute(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }
}
