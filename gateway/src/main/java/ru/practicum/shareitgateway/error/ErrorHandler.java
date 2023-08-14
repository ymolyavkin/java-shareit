package ru.practicum.shareitgateway.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareitgateway.exception.BadRequestException;
import ru.practicum.shareitgateway.exception.CommentErrorException;
import ru.practicum.shareitgateway.exception.NoneXSharerUserIdException;
import ru.practicum.shareitgateway.exception.UnsupportedStatusException;

import javax.validation.ValidationException;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler({ValidationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidation(final RuntimeException e) {
        log.debug("Получен статус 400 BAD_REQUEST {}", e.getMessage(), e);
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler({NoneXSharerUserIdException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleNoneXSharerUserId(final RuntimeException e) {
        log.debug("Получен статус 500 INTERNAL_SERVER_ERROR {}", e.getMessage(), e);
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler({UnsupportedStatusException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleUnsupportedStatusException(final RuntimeException e) {
        log.debug("Unknown state: UNSUPPORTED_STATUS. Статус ошибки 500 INTERNAL_SERVER_ERROR{}", e.getMessage(), e);
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler({BadRequestException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleNotAvailable(final RuntimeException e) {
        log.debug("Ошибка бронирования - вещь недоступна для бронирования. Статус ошибки 400 Bad Request{}", e.getMessage(), e);
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler({CommentErrorException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleCommentErrorException(final RuntimeException e) {
        log.debug("Ошибка сохранения комментария. Статус ошибки 400 Bad Request{}", e.getMessage(), e);
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler({IllegalStateException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleIllegalStateException(final RuntimeException e) {
        log.debug("Ошибка IllegalStateException. Статус ошибки 400 Bad Request{}", e.getMessage(), e);
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler({IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleIllegalArgumentException(final RuntimeException e) {
        log.debug("Ошибка IllegalArgumentException. Статус ошибки 400 Bad Request{}", e.getMessage(), e);
        return Map.of("error", e.getMessage());
    }
}