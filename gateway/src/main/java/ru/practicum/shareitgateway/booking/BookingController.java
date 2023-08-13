package ru.practicum.shareitgateway.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.UnsatisfiedServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareitgateway.booking.dto.IncomingBookingDto;
import ru.practicum.shareitgateway.booking.model.State;
import ru.practicum.shareitgateway.validator.Marker;

import javax.validation.constraints.Min;

import static ru.practicum.shareitgateway.util.Constants.USER_ID_FROM_REQUEST;


@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingClient bookingClient;

    @GetMapping
    public ResponseEntity<Object> getBookingsByBooker(
            @RequestHeader(value = USER_ID_FROM_REQUEST) Long bookerId,
            @RequestParam(defaultValue = "ALL") State state,
            @RequestParam(name = "from", defaultValue = "0") @Min(0) int from,
            @RequestParam(name = "size", defaultValue = "10") @Min(1) int size) {
        log.info("Получен запрос на выдачу вещей, забронированных пользователем с id = {}", bookerId);
        return bookingClient.getBookingsByBooker(bookerId, state, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getBookingsByOwner(
            @RequestHeader(value = USER_ID_FROM_REQUEST) Long ownerId,
            @RequestParam(defaultValue = "ALL") State state,
            @RequestParam(name = "from", defaultValue = "0") @Min(0) int from,
            @RequestParam(name = "size", defaultValue = "10") @Min(1) int size) {
        log.info("Получен запрос на выдачу вещей, принадлежащих пользователю с id = {}", ownerId);
        return bookingClient.getBookingsByOwner(ownerId, state, from, size);
    }

    @ExceptionHandler(UnsatisfiedServletRequestParameterException.class)
    @PostMapping
    public ResponseEntity<Object> addBooking(@RequestHeader(value = USER_ID_FROM_REQUEST) Long bookerId,
                                             @Validated(Marker.OnCreate.class) @RequestBody IncomingBookingDto incomingBookingDto) {
        log.info("Получен запрос на бронирование вещи пользователем с id = {}", bookerId);

        return bookingClient.addBooking(bookerId, incomingBookingDto);
    }

    @PatchMapping(value = "/{bookingId}")
    public ResponseEntity<Object> approveBooking(@RequestHeader(value = USER_ID_FROM_REQUEST) Long ownerId,
                                                 @PathVariable Long bookingId,
                                                 @RequestParam(required = false) Boolean approved) {

        log.info("Получен запрос на подтверждение бронирования id = {} пользователем с id = {}", bookingId, ownerId);

        return bookingClient.approveBooking(ownerId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBookingById(@RequestHeader(value = USER_ID_FROM_REQUEST) Long userId,
                                                 @PathVariable Long bookingId) {
        log.info("Получен запрос на данных о бронировании с id = {}", bookingId);

        return bookingClient.getBookingById(bookingId, userId);
    }
}
