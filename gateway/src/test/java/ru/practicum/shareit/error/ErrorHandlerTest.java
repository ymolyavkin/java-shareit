package ru.practicum.shareit.error;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import ru.practicum.shareitgateway.exception.*;
import ru.practicum.shareitgateway.error.ErrorHandler;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.MockitoAnnotations.initMocks;

class ErrorHandlerTest {
    @InjectMocks
    private ErrorHandler errorHandler;


    @BeforeEach
    public void beforeEach() {
        initMocks(this);
    }

    @Test
    void handleNotFoundTest() {
        NotFoundException notFoundException = new NotFoundException("Статус ошибки 404 Not found");
        Map<String, String> response = errorHandler.handleNotFound(notFoundException);

        assertEquals("Статус ошибки 404 Not found", response.get("error"));
    }

    @Test
    void handleValidationTest() {
        ValidationException validationException = new ValidationException("Ошибка валидации");
        Map<String, String> response = errorHandler.handleValidation(validationException);

        assertEquals("Ошибка валидации", response.get("error"));
    }

    @Test
    void handleNoneXSharerUserIdTest() {
        NoneXSharerUserIdException noneXSharerUserIdException = new NoneXSharerUserIdException("noneXSharerUserIdException");
        Map<String, String> response = errorHandler.handleNoneXSharerUserId(noneXSharerUserIdException);

        assertEquals("noneXSharerUserIdException", response.get("error"));
    }

    @Test
    void handleUnsupportedStatusExceptionTest() {
        UnsupportedStatusException unsupportedStatusException = new UnsupportedStatusException("UnsupportedStatusException");
        Map<String, String> response = errorHandler.handleUnsupportedStatusException(unsupportedStatusException);

        assertEquals("UnsupportedStatusException", response.get("error"));
    }

    @Test
    void handleEntityNotFoundTest() {
        EntityNotFoundException entityNotFoundException = new EntityNotFoundException("EntityNotFound");
        Map<String, String> response = errorHandler.handleNotFound(entityNotFoundException);

        assertEquals("EntityNotFound", response.get("error"));
    }

    @Test
    void handleCommentErrorException() {
        CommentErrorException commentErrorException = new CommentErrorException("CommentErrorException");
        Map<String, String> response = errorHandler.handleCommentErrorException(commentErrorException);

        assertEquals("CommentErrorException", response.get("error"));
    }

    @Test
    void handleConstraintViolationExceptionTest() {
        ConstraintViolationException constraintViolationException = new ConstraintViolationException(Set.of());
        Map<String, String> response = errorHandler.handleConstraintViolationException(constraintViolationException);

        assertNotNull(response.get("error"));
    }

    @Test
    void handleIllegalStateExceptionTest() {
        IllegalStateException illegalStateException = new IllegalStateException("IllegalStateException");
        Map<String, String> response = errorHandler.handleIllegalStateException(illegalStateException);

        assertEquals("IllegalStateException", response.get("error"));
    }

    @Test
    void handleIllegalArgumentExceptionTest() {
        IllegalArgumentException illegalArgumentException = new IllegalArgumentException("IllegalArgumentException");
        Map<String, String> response = errorHandler.handleIllegalArgumentException(illegalArgumentException);

        assertEquals("IllegalArgumentException", response.get("error"));
    }

    @Test
    void handleNotAvailableExceptionTest() {
        OwnerMismatchException ownerMismatchException = new OwnerMismatchException("OwnerMismatchException");
        Map<String, String> response = errorHandler.handleNotAvailable(ownerMismatchException);

        assertEquals("OwnerMismatchException", response.get("error"));
    }
}