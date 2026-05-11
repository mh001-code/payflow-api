package com.payflow.domain.exception;

public class DuplicateUserException extends RuntimeException {

    public DuplicateUserException(String field, String value) {
        super("User already exists with " + field + ": " + value);
    }
}
