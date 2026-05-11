package com.payflow.application.port.out;

import com.payflow.domain.model.User;

import java.util.Optional;

public interface UserRepositoryPort {

    User save(User user);

    Optional<User> findById(Long id);

    boolean existsByCpf(String cpf);

    boolean existsByEmail(String email);
}
