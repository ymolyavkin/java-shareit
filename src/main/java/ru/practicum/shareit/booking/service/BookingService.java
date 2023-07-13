package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.dto.IncomingBookingDto;

import java.util.List;

public interface BookingService {
    List<Booking> getAll();

    BookingResponseDto getBookingById(Long id, Long userId);

    List<BookingResponseDto> getBookingsByBooker(Long bookerId);

    BookingResponseDto addBooking(IncomingBookingDto incomingBookingDto);

    BookingResponseDto updateBooking(IncomingBookingDto incomingBookingDto, Long bookingId, Long bookerId);

    BookingResponseDto approvingBooking(Long bookingId, Long bookerId, boolean approved);

    List<BookingResponseDto> getBookingsByOwner(Long ownerId);
}
