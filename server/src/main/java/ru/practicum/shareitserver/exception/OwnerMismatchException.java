package ru.practicum.shareitserver.exception;

public class OwnerMismatchException extends RuntimeException {
    public OwnerMismatchException(String message) {
        super(message);
    }
}

