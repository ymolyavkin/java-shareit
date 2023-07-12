package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.UnsatisfiedServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingWithItemIdAndNameDto;
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
    public BookingWithItemIdAndNameDto addBooking(@RequestHeader(value = USER_ID_FROM_REQUEST, defaultValue = "-1") Long bookerId,
                                                  @Valid @RequestBody IncomingBookingDto incomingBookingDto) {
        log.info("Получен запрос на бронирование вещи пользователем с id = {}", bookerId);
        if (bookerId.equals(-1L)) {
            throw new NoneXSharerUserIdException("Не указан инициатор бронирования вещи");
        }
        incomingBookingDto.setBookerId(bookerId);

        return bookingService.addBooking(incomingBookingDto);
    }

    @PatchMapping(value = "/{bookingId}", consumes = "application/json")
    public BookingWithItemIdAndNameDto updateBooking(@RequestHeader(value = USER_ID_FROM_REQUEST, defaultValue = "-1") Long ownerId,
                                    @RequestBody(required = false) IncomingBookingDto incomingBookingDto,
                                    @PathVariable Long bookingId,
                                    @RequestParam(required = false) Boolean approved) {
        if (ownerId.equals(-1L)) {
            throw new NoneXSharerUserIdException("Не указан владелец вещи");
        }
        if (approved != null) {
            log.info("Получен запрос на подтверждение бронирования id = {} пользователем с id = {}", bookingId, ownerId);
            return bookingService.approvingBooking(bookingId, ownerId, approved);
        }
        log.info("Получен запрос на обновление вещи пользователем с id = {}", ownerId);
        return bookingService.updateBooking(incomingBookingDto, bookingId, ownerId);
    }

    @GetMapping("/{id}")
    public BookingWithItemIdAndNameDto getBookingById(@PathVariable Long id) {
        log.info("Получен запрос на выдачу бронирования с id = {}", id);

        return bookingService.getBookingById(id);
    }

}
