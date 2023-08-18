package ru.practicum.shareitgateway.exception;

public class CommentErrorException extends RuntimeException {
    public CommentErrorException(String message) {
        super(message);
    }
}