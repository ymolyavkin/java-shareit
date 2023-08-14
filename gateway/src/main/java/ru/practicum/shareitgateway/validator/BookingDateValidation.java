package ru.practicum.shareitgateway.validator;

import ru.practicum.shareitgateway.booking.dto.IncomingBookingDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class BookingDateValidation implements ConstraintValidator<StartBeforeEndValidation, IncomingBookingDto> {

    @Override
    public boolean isValid(IncomingBookingDto bookingDto, ConstraintValidatorContext constraintValidatorContext) {
        LocalDateTime start = bookingDto.getStart();
        LocalDateTime end = bookingDto.getEnd();
        if (start == null || end == null) {
            return false;
        }
        return start.isBefore(end);
    }
}