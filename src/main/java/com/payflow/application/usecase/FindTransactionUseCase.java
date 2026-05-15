package com.payflow.application.usecase;

import com.payflow.application.port.out.TransactionRepositoryPort;
import com.payflow.domain.exception.TransactionNotFoundException;
import com.payflow.domain.model.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FindTransactionUseCase {

    private final TransactionRepositoryPort transactionRepository;

    @Transactional(readOnly = true)
    public Transaction findById(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public List<Transaction> findByUserId(Long userId) {
        return transactionRepository.findByUserId(userId);
    }
}
