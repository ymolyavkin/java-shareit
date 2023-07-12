package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingWithItemIdAndNameDto;
import ru.practicum.shareit.booking.dto.IncomingBookingDto;

import java.util.List;

public interface BookingService {
    List<Booking> getAll();

    BookingWithItemIdAndNameDto getBookingById(Long id);

    List<BookingDto> getBookingsByBooker(Long bookerId);

    BookingWithItemIdAndNameDto addBooking(IncomingBookingDto incomingBookingDto);

    BookingWithItemIdAndNameDto updateBooking(IncomingBookingDto incomingBookingDto, Long bookingId, Long bookerId);

    BookingWithItemIdAndNameDto approvingBooking(Long bookingId, Long bookerId, boolean approved);
}
