package ru.practicum.shareitgateway.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareitgateway.booking.dto.IncomingBookingDto;
import ru.practicum.shareitgateway.booking.model.State;
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
        log.info("Добавить бронирование от пользователя с id = {} ", bookerId);
        return post("", bookerId, incomingBookingDto);
    }

    public ResponseEntity<Object> getBookingsByBooker(long userId, State state, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "state", state.name(),
                "from", from,
                "size", size
        );
        log.info("Получить список всех бронирований пользователя с id = {} ", userId);
        return get("?state={state}&from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> getBookingById(long userId, Long bookingId) {
        log.info("Получить бронирование с id = {} автор бронирования id = {}", bookingId, userId);
        return get("/" + bookingId, userId);
    }


    public ResponseEntity<Object> getBookingsByOwner(long userId, State state, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "state", state.name(),
                "from", from,
                "size", size
        );
        log.info("Получить список бронирований пользователя с id = {} ", userId);
        return get("/owner?state={state}&from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> approveBooking(long userId, Long bookingId, Boolean approved) {
        Map<String, Object> parameters = Map.of("approved", approved);
        log.info("Подтвердить пользователем с id = {}  бронирование c id = {}", userId, bookingId);
        return patch("/" + bookingId + "?approved={approved}", userId, parameters, null);
    }
}
