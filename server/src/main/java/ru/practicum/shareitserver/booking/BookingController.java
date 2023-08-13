package ru.practicum.shareitserver.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.UnsatisfiedServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareitserver.booking.dto.BookingResponseDto;
import ru.practicum.shareitserver.booking.dto.IncomingBookingDto;
import ru.practicum.shareitserver.booking.model.StateRequest;
import ru.practicum.shareitserver.booking.service.BookingService;
import ru.practicum.shareitserver.exception.NoneXSharerUserIdException;

import javax.validation.constraints.Min;
import java.util.List;

import static ru.practicum.shareitserver.util.Constants.USER_ID_FROM_REQUEST;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    @GetMapping
    public List<BookingResponseDto> getBookingsByBooker(
            @RequestHeader(value = USER_ID_FROM_REQUEST) Long bookerId,
            @RequestParam(defaultValue = "ALL") StateRequest state,
            @RequestParam(name = "from", defaultValue = "0") @Min(0) int from,
            @RequestParam(name = "size", defaultValue = "10") @Min(1) int size) {
        log.info("Получен запрос на выдачу вещей, забронированных пользователем с id = {}", bookerId);
        return bookingService.getBookingsByBooker(bookerId, state, from, size);
    }

    @GetMapping("/owner")
    public List<BookingResponseDto> getBookingsByOwner(
            @RequestHeader(value = USER_ID_FROM_REQUEST) Long ownerId,
            @RequestParam(defaultValue = "ALL") StateRequest state,
            @RequestParam(name = "from", defaultValue = "0") @Min(0) int from,
            @RequestParam(name = "size", defaultValue = "10") @Min(1) int size) {
        log.info("Получен запрос на выдачу вещей, принадлежащих пользователю с id = {}", ownerId);
        return bookingService.getBookingsByOwner(ownerId, state, from, size);
    }

    @ExceptionHandler(UnsatisfiedServletRequestParameterException.class)
    @PostMapping
    public BookingResponseDto addBooking(@RequestHeader(value = USER_ID_FROM_REQUEST) Long bookerId,
                                         @RequestBody IncomingBookingDto incomingBookingDto) {
        log.info("Получен запрос на бронирование вещи пользователем с id = {}", bookerId);
        if (bookerId.equals(-1L)) {
            throw new NoneXSharerUserIdException("Не указан инициатор бронирования вещи");
        }
        incomingBookingDto.setBookerId(bookerId);

        return bookingService.addBooking(incomingBookingDto);
    }

    @PatchMapping(value = "/{bookingId}", consumes = "application/json")
    public BookingResponseDto updateBooking(@RequestHeader(value = USER_ID_FROM_REQUEST) Long ownerId,
                                            @RequestBody(required = false) IncomingBookingDto incomingBookingDto,
                                            @PathVariable Long bookingId,
                                            @RequestParam(required = false) Boolean approved) {

        log.info("Получен запрос на обновление вещи пользователем с id = {}", ownerId);
        if (approved != null) {
            log.info("Получен запрос на подтверждение бронирования id = {} пользователем с id = {}", bookingId, ownerId);
            return bookingService.approvingBooking(bookingId, ownerId, approved);
        }
        return bookingService.updateBooking(incomingBookingDto, bookingId, ownerId);
    }

    @GetMapping("/{bookingId}")
    public BookingResponseDto getBookingById(@RequestHeader(value = USER_ID_FROM_REQUEST) Long userId,
                                             @PathVariable Long bookingId) {
        log.info("Получен запрос на данных о бронировании с id = {}", bookingId);

        return bookingService.getBookingById(bookingId, userId);
    }
}