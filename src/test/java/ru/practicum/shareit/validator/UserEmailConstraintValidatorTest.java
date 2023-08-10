package ru.practicum.shareit.validator;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserEmailConstraintValidatorTest {
    private UserEmailConstraintValidator validator = new UserEmailConstraintValidator();

    @Nested
    class UserEmailValid {
        @Test
        void isValid_shouldReturnTrue_whenNoWhitespaces() {
            assertTrue(isValid("email@mail.ru"));
            assertTrue(isValid("foo@bar.com"));
        }
    }

    private boolean isValid(String value) {
        return validator.isValid(value, null);
    }
}