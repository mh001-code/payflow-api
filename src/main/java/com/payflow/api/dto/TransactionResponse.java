package com.payflow.api.dto;

import com.payflow.domain.model.Transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionResponse(
        Long id,
        Long payerId,
        Long payeeId,
        BigDecimal amount,
        String status,
        LocalDateTime createdAt
) {

    public static TransactionResponse from(Transaction transaction, Long payerId, Long payeeId) {
        return new TransactionResponse(
                transaction.getId(),
                payerId,
                payeeId,
                transaction.getAmount(),
                transaction.getStatus().name(),
                transaction.getCreatedAt()
        );
    }
}
