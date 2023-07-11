package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingWithItemIdAndNameDto;
import ru.practicum.shareit.booking.dto.BookingWithItemMapDto;
import ru.practicum.shareit.booking.dto.IncomingBookingDto;

import java.util.List;

public interface BookingService {
      List<Booking> getAll();


    BookingDto getBookingById(Long id);

    List<BookingDto> getBookingsByBooker(Long bookerId);

 //   BookingWithItemIdAndNameDto addBooking(IncomingBookingDto incomingBookingDto);
    BookingWithItemMapDto addBookingWithItem(IncomingBookingDto incomingBookingDto);
    BookingDto updateBooking(IncomingBookingDto incomingBookingDto, Long bookingId, Long bookerId);
}
