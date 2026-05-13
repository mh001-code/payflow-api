package com.payflow.domain.exception;

public class UnauthorizedTransactionException extends RuntimeException {

    public UnauthorizedTransactionException() {
        super("Lojistas não podem realizar transferências");
    }

    public UnauthorizedTransactionException(String message) {
        super(message);
    }
}
