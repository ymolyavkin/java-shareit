package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.IncomingBookingDto;

import java.util.List;

public interface BookingService {
      List<Booking> getAll();


    Booking getBookingById(Long id);

    List<BookingDto> getBookingsByBooker(Long bookerId);

    BookingDto addBooking(IncomingBookingDto incomingBookingDto);

    BookingDto updateBooking(IncomingBookingDto incomingBookingDto, Long bookingId, Long bookerId);
}
