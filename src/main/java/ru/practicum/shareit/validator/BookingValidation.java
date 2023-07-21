package ru.practicum.shareit.validator;

import ru.practicum.shareit.booking.dto.IncomingBookingDto;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;

public class BookingValidation {
    public static void bookingIsValid(IncomingBookingDto incomingBookingDto, Item item) {
        if (!item.getAvailable()) {
            throw new BadRequestException("Вещь недоступна для бронирования");
        }
        if (incomingBookingDto.getStart() == null || incomingBookingDto.getEnd() == null) {
            throw new BadRequestException("Дата бронирования не задана");
        }
        if (incomingBookingDto.getEnd().isBefore(incomingBookingDto.getStart())) {
            throw new BadRequestException("Указано время окончания бронирования раньше начала бронирования");
        }
        if (incomingBookingDto.getEnd().isBefore(LocalDateTime.now())
                || incomingBookingDto.getStart().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Указано время бронирования раньше текущей даты");
        }
        if (incomingBookingDto.getEnd().isEqual(incomingBookingDto.getStart())) {
            throw new BadRequestException("Указано окончание бронирования, совпадающее с датой начала бронирования");
        }
    }
}
