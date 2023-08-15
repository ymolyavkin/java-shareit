package ru.practicum.shareitgateway.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ElementType.TYPE_USE, ElementType.PARAMETER})
@Retention(RUNTIME)
@Constraint(validatedBy = BookingDateValidation.class)
public @interface StartBeforeEndValidation {
    String message() default "Начало бронирования должно быть раньше окончания и не null";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}