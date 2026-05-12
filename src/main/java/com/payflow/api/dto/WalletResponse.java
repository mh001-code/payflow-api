package com.payflow.api.dto;

import com.payflow.domain.model.Wallet;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record WalletResponse(
        Long id,
        Long userId,
        BigDecimal balance,
        LocalDateTime createdAt
) {

    public static WalletResponse from(Wallet wallet) {
        return new WalletResponse(
                wallet.getId(),
                wallet.getUser().getId(),
                wallet.getBalance(),
                wallet.getCreatedAt()
        );
    }
}
