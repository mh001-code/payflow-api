package com.payflow.application.port.out;

import com.payflow.domain.model.Transaction;

public interface AuthorizationPort {
    boolean authorize(Transaction transaction);
}
