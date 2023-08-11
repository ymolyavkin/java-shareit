package ru.practicum.shareitgateway.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareitgateway.booking.dto.IncomingBookingDto;
import ru.practicum.shareitgateway.booking.dto.State;
import ru.practicum.shareitgateway.client.BaseClient;

import java.util.Map;
@Slf4j
@Service
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> addBooking(long bookerId, IncomingBookingDto incomingBookingDto) {
        log.info("Create booking for user with id = {} ", bookerId);
        return post("", bookerId, incomingBookingDto);
    }

    public ResponseEntity<Object> getBookings(long userId, State state, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "state", state.name(),
                "from", from,
                "size", size
        );
        log.info("Get all booking for user with id = {} ", userId);
        return get("?state={state}&from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> getBooking(long userId, Long bookingId) {
        log.info("Get booking for user with id = {} with booking id = {}", userId, bookingId);
        return get("/" + bookingId, userId);
    }


    public ResponseEntity<Object> getBookingByOwner(long userId, State state, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "state", state.name(),
                "from", from,
                "size", size
        );
        log.info("Get all booking for owner with id = {} ", userId);
        return get("/owner?state={state}&from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> approveBooking(long userId, Long bookingId, Boolean approved) {
        Map<String, Object> parameters = Map.of("approved", approved);
        log.info("Get all booking for user with id = {} with booking id = {}", userId, bookingId);
        return patch("/" + bookingId + "?approved={approved}", userId, parameters, null);
    }
}
