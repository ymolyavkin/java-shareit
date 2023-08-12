package ru.practicum.shareitserver.exception;

public class CommentErrorException extends RuntimeException {
    public CommentErrorException(String message) {
        super(message);
    }
}