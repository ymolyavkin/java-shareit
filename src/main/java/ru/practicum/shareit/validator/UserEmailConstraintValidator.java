package ru.practicum.shareit.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UserEmailConstraintValidator implements ConstraintValidator<UserEmailConstraint, String> {
    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        return email == null;
    }
}
/*
ConstraintValidator<UserLoginConstraint, String>
@Override
    public boolean isValid(String login, ConstraintValidatorContext constraintValidatorContext) {
        return login == null || !login.contains(" ");
    }
}
 */