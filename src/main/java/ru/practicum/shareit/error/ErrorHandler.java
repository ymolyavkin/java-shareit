package ru.practicum.shareit.error;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.exception.AlreadyExistsException;
import ru.practicum.shareit.exception.NoneXSharerUserIdException;
import ru.practicum.shareit.exception.NotFoundException;


import javax.persistence.EntityNotFoundException;
import javax.validation.ValidationException;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler({NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotFound(final RuntimeException e) {
        log.debug("Статус ошибки 404 Not found");
        return Map.of(
                "error", e.getMessage()
        );
    }

    @ExceptionHandler({AlreadyExistsException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> handleAlreadyExists(final RuntimeException e) {
        return Map.of(
                "error", e.getMessage()
        );
    }

    @ExceptionHandler({ValidationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidation(final RuntimeException e) {
        return Map.of(
                "error", e.getMessage()
        );
    }

    @ExceptionHandler({NoneXSharerUserIdException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleNoneXSharerUserId(final RuntimeException e) {
        return Map.of(
                "error", e.getMessage()
        );
    }
    @ExceptionHandler({ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleConstraintViolationException(final RuntimeException e) {
        log.debug("Нарушение ограничения таблицы БД. Статус ошибки 500 INTERNAL_SERVER_ERROR.");
        return Map.of(
                "error", e.getMessage()
        );
    }

    @ExceptionHandler({JDBCConnectionException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleJDBCConnectionException(final RuntimeException e) {
        log.debug("Нарушение ограничений БД. Статус ошибки 500 INTERNAL_SERVER_ERROR");
        return Map.of(
                "error", "Нарушение ограничений БД. Статус ошибки 500 INTERNAL_SERVER_ERROR"
        );
    }
    //EntityNotFoundException
    @ExceptionHandler({EntityNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleEntityNotFound(final RuntimeException e) {
        log.debug("Ошибка БД. Статус ошибки 404 Not found");
        return Map.of(
                "error", "Ошибка БД. Статус ошибки 404 Not found"
        );
    }
}
