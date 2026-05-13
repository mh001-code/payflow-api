package com.payflow.application.port.out;

import com.payflow.domain.model.Transaction;

public interface NotificationPort {
    void notify(Transaction transaction);
}
