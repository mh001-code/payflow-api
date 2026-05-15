package com.payflow.infrastructure.persistence;

import com.payflow.application.port.out.TransactionRepositoryPort;
import com.payflow.domain.model.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
class TransactionRepositoryAdapter implements TransactionRepositoryPort {

    private final JpaTransactionRepository repository;

    @Override
    public Transaction save(Transaction transaction) {
        return repository.save(transaction);
    }

    @Override
    public Optional<Transaction> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<Transaction> findByUserId(Long userId) {
        return repository.findByPayerUserIdOrPayeeUserId(userId, userId);
    }
}
