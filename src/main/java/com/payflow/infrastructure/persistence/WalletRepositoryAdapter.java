package com.payflow.infrastructure.persistence;

import com.payflow.application.port.out.WalletRepositoryPort;
import com.payflow.domain.model.Wallet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
class WalletRepositoryAdapter implements WalletRepositoryPort {

    private final JpaWalletRepository jpa;

    @Override
    public Wallet save(Wallet wallet) {
        return jpa.save(wallet);
    }

    @Override
    public Optional<Wallet> findById(Long id) {
        return jpa.findById(id);
    }

    @Override
    public Optional<Wallet> findByUserId(Long userId) {
        return jpa.findByUserId(userId);
    }

    @Override
    public boolean existsByUserId(Long userId) {
        return jpa.existsByUserId(userId);
    }
}
