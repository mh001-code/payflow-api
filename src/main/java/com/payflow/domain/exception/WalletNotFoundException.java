package com.payflow.domain.exception;

public class WalletNotFoundException extends RuntimeException {

    public WalletNotFoundException(Long id) {
        super("Wallet not found with id: " + id);
    }

    public WalletNotFoundException(String message) {
        super(message);
    }
}
