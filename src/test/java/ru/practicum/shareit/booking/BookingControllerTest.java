package ru.practicum.shareit.booking;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.dto.IncomingBookingDto;
import ru.practicum.shareit.booking.service.BookingService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;

@WebMvcTest(controllers = BookingControllerTest.class)
class BookingControllerTest {
    @MockBean
    BookingService bookingService;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    private final EasyRandom generator = new EasyRandom();

    @Test
    void getBookingsByBooker() {
        /*
         @GetMapping
    public List<BookingResponseDto> getBookingsByBooker(@RequestHeader(value = USER_ID_FROM_REQUEST, defaultValue = "-1") Long bookerId,
                                                        @RequestParam(required = false, defaultValue = "ALL") StateRequest state) {
        log.info("Получен запрос на выдачу вещей, забронированных пользователем с id = {}", bookerId);
        return bookingService.getBookingsByBooker(bookerId, state);
         */
    }

    @Test
    void getBookingsByOwner() {
    }

    @Test
    void addBooking() throws Exception {
        /*
        public BookingResponseDto addBooking(@RequestHeader(value = USER_ID_FROM_REQUEST, defaultValue = "-1") Long bookerId,
                                         @Valid @RequestBody IncomingBookingDto incomingBookingDto) {
        log.info("Получен запрос на бронирование вещи пользователем с id = {}", bookerId);
        if (bookerId.equals(-1L)) {
            throw new NoneXSharerUserIdException("Не указан инициатор бронирования вещи");
        }
        incomingBookingDto.setBookerId(bookerId);

        return bookingService.addBooking(incomingBookingDto);
         */
        IncomingBookingDto incomingBookingDto = new IncomingBookingDto();//generator.nextObject(IncomingBookingDto.class);
        incomingBookingDto.setItemId(1L);
        incomingBookingDto.setStart(LocalDateTime.now().plus(1, ChronoUnit.DAYS));
        incomingBookingDto.setEnd(LocalDateTime.now().plus(2, ChronoUnit.DAYS));

        incomingBookingDto.setBookerId(1L);

        when(bookingService.addBooking(Mockito.any(IncomingBookingDto.class)))
                .thenAnswer(invocationOnMock -> {
                    BookingResponseDto bookingResponseDto = invocationOnMock.getArgument(0, BookingResponseDto.class);
                    // incomingDto.setItemId(1L);
                    bookingResponseDto.setId(1L);
                    return bookingResponseDto;
                });
        mockMvc.perform(post("/bookings")
                        .content(objectMapper.writeValueAsString((incomingBookingDto)))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1L)));
    }

    @Test
    void updateBooking() {
    }

    @Test
    void getBookingById() {
    }
}