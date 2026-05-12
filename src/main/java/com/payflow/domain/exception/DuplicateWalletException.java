package com.payflow.domain.exception;

public class DuplicateWalletException extends RuntimeException {

    public DuplicateWalletException(Long userId) {
        super("Wallet already exists for user: " + userId);
    }
}
