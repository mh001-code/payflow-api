package com.payflow.application.port.out;

import com.payflow.domain.model.Wallet;

import java.util.Optional;

public interface WalletRepositoryPort {

    Wallet save(Wallet wallet);

    Optional<Wallet> findById(Long id);

    Optional<Wallet> findByUserId(Long userId);

    boolean existsByUserId(Long userId);
}
