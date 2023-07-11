package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.UnsatisfiedServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingWithItemIdAndNameDto;
import ru.practicum.shareit.booking.dto.BookingWithItemMapDto;
import ru.practicum.shareit.booking.dto.IncomingBookingDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.NoneXSharerUserIdException;


import javax.validation.Valid;
import java.util.List;

import static ru.practicum.shareit.util.Constants.USER_ID_FROM_REQUEST;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;
    @GetMapping
    public List<BookingDto> getBookings(@RequestHeader(value = USER_ID_FROM_REQUEST, defaultValue = "-1") Long bookerId) {
        log.info("Получен запрос на выдачу вещей, забронированных пользователем с id = {}", bookerId);
        return bookingService.getBookingsByBooker(bookerId);
    }

    @ExceptionHandler(UnsatisfiedServletRequestParameterException.class)
    @PostMapping
    public BookingWithItemMapDto addBooking(@RequestHeader(value = USER_ID_FROM_REQUEST, defaultValue = "-1") Long bookerId,
                                            @Valid @RequestBody IncomingBookingDto incomingBookingDto) {
        log.info("Получен запрос на бронирование вещи пользователем с id = {}", bookerId);
        if (bookerId.equals(-1L)) {
            throw new NoneXSharerUserIdException("Не указан инициатор бронирования вещи");
        }
        incomingBookingDto.setBookerId(bookerId);
       // return bookingService.addBookingWithItem(incomingBookingDto);
        return bookingService.addBookingWithItem(incomingBookingDto);
    }

    @PatchMapping(value = "/{BookingId}", consumes = "application/json")
    public BookingDto updateBooking(@RequestHeader(value = USER_ID_FROM_REQUEST, defaultValue = "-1") Long bookerId,
                                                     @RequestBody IncomingBookingDto incomingBookingDto,
                                                     @PathVariable Long BookingId) {
        log.info("Получен запрос на обновление вещи пользователя с id = {}", bookerId);
        if (bookerId.equals(-1L)) {
            throw new NoneXSharerUserIdException("Не указан инициатор бронирования вещи");
        }
        return bookingService.updateBooking(incomingBookingDto, BookingId, bookerId);
    }

    @GetMapping("/{id}")
    public BookingDto getBookingById(@PathVariable Long id) {
        log.info("Получен запрос на выдачу вещи с id = {}", id);

        return bookingService.getBookingById(id);
    }

}
