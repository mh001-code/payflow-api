package com.payflow.infrastructure.persistence;

import com.payflow.domain.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

interface JpaTransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByPayerUserIdOrPayeeUserId(Long payerUserId, Long payeeUserId);
}
