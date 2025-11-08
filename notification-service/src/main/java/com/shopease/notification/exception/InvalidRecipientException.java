package com.shopease.notification.exception;

public class InvalidRecipientException extends RuntimeException {
    public InvalidRecipientException(String message) {
        super(message);
    }
}