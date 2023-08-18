package ru.practicum.shareitgateway.exception;

public class OwnerMismatchException extends RuntimeException {
    public OwnerMismatchException(String message) {
        super(message);
    }
}

