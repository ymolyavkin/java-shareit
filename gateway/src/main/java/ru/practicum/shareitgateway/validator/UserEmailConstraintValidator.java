package ru.practicum.shareitgateway.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UserEmailConstraintValidator implements ConstraintValidator<UserEmailConstraint, String> {
    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        return email != null;
    }
}
