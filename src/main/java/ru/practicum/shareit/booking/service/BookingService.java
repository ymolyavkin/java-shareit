package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.dto.IncomingBookingDto;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.StateRequest;

import java.util.List;

public interface BookingService {
    List<Booking> getAll();

    BookingResponseDto getBookingById(Long id, Long userId);

    List<BookingResponseDto> getBookingsByBooker(Long bookerId, StateRequest state);

    BookingResponseDto addBooking(IncomingBookingDto incomingBookingDto);

    BookingResponseDto updateBooking(IncomingBookingDto incomingBookingDto, Long bookingId, Long bookerId);

    BookingResponseDto approvingBooking(Long bookingId, Long bookerId, Boolean approved);

   // List<BookingResponseDto> getBookingsByOwner(Long ownerId, String state);

    List<BookingResponseDto> getBookingsByOwner(Long ownerId, StateRequest state);
}
