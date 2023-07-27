package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.UnsatisfiedServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.dto.IncomingBookingDto;
import ru.practicum.shareit.booking.model.StateRequest;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.NoneXSharerUserIdException;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

import static ru.practicum.shareit.util.Constants.USER_ID_FROM_REQUEST;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    @GetMapping
    public List<BookingResponseDto> getBookingsByBooker(@RequestHeader(value = USER_ID_FROM_REQUEST, defaultValue = "-1") Long bookerId,
                                                        @Validated
                                                        @RequestParam(required = false, defaultValue = "ALL") StateRequest state,
                                                        @RequestParam(name = "from", defaultValue = "0", required = false) @Min(0) Integer from,
                                                        @RequestParam(name = "size", defaultValue = "10", required = false) @Min(1) Integer size) {
        log.info("Получен запрос на выдачу вещей, забронированных пользователем с id = {}", bookerId);
        return bookingService.getBookingsByBooker(bookerId, state, from, size);
    }

    @GetMapping("/owner")
    public List<BookingResponseDto> getBookingsByOwner(@RequestHeader(value = USER_ID_FROM_REQUEST, defaultValue = "-1") Long ownerId,
                                                       @Validated
                                                       @RequestParam(required = false, defaultValue = "ALL") StateRequest state,
                                                       @RequestParam(name = "from", defaultValue = "0", required = false) @Min(0) Integer from,
                                                       @RequestParam(name = "size", defaultValue = "10", required = false) @Min(1) Integer size) {
        log.info("Получен запрос на выдачу вещей, принадлежащих пользователю с id = {}", ownerId);
        return bookingService.getBookingsByOwner(ownerId, state, from, size);
    }

    @Validated
    @ExceptionHandler(UnsatisfiedServletRequestParameterException.class)
    @PostMapping
    public BookingResponseDto addBooking(@RequestHeader(value = USER_ID_FROM_REQUEST, defaultValue = "-1") Long bookerId,
                                         @Valid @RequestBody IncomingBookingDto incomingBookingDto) {
        log.info("Получен запрос на бронирование вещи пользователем с id = {}", bookerId);
        if (bookerId.equals(-1L)) {
            throw new NoneXSharerUserIdException("Не указан инициатор бронирования вещи");
        }
        incomingBookingDto.setBookerId(bookerId);

        return bookingService.addBooking(incomingBookingDto);
    }

    @Validated
    @PatchMapping(value = "/{bookingId}", consumes = "application/json")
    public BookingResponseDto updateBooking(@RequestHeader(value = USER_ID_FROM_REQUEST, defaultValue = "-1") Long ownerId,
                                            @RequestBody(required = false) IncomingBookingDto incomingBookingDto,
                                            @PathVariable Long bookingId,
                                            @RequestParam(required = false) Boolean approved) {
        if (ownerId.equals(-1L)) {
            throw new NoneXSharerUserIdException("Не указан владелец вещи");
        }
        log.info("Получен запрос на обновление вещи пользователем с id = {}", ownerId);
        if (approved != null) {
            log.info("Получен запрос на подтверждение бронирования id = {} пользователем с id = {}", bookingId, ownerId);
            return bookingService.approvingBooking(bookingId, ownerId, approved);
        }
        return bookingService.updateBooking(incomingBookingDto, bookingId, ownerId);
    }

    @GetMapping("/{bookingId}")
    public BookingResponseDto getBookingById(@RequestHeader(value = USER_ID_FROM_REQUEST, defaultValue = "-1") Long userId,
                                             @PathVariable Long bookingId) {
        log.info("Получен запрос на данных о бронировании с id = {}", bookingId);

        return bookingService.getBookingById(bookingId, userId);
    }
}