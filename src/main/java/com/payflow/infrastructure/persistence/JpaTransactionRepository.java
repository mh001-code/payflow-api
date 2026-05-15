package com.payflow.infrastructure.persistence;

import com.payflow.domain.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

interface JpaTransactionRepository extends JpaRepository<Transaction, Long> {
}
