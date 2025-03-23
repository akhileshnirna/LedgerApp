package org.example.ledgerapp.exception;

public class InvalidAccountTypeException extends IllegalArgumentException {
    public InvalidAccountTypeException(String message) {
        super(message);
    }

}
