package com.payflow.infrastructure.persistence;

import com.payflow.domain.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

interface JpaWalletRepository extends JpaRepository<Wallet, Long> {

    Optional<Wallet> findByUserId(Long userId);

    boolean existsByUserId(Long userId);
}
