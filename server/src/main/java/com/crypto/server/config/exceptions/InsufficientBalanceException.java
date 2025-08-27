package com.crypto.server.config.exceptions;

public class InsufficientBalanceException extends CryptoException {
    public InsufficientBalanceException(String message) { super(message); }
}
