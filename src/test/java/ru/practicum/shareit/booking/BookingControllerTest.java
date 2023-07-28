package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookerDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.dto.IncomingBookingDto;
import ru.practicum.shareit.booking.model.StateRequest;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemIdNameDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.practicum.shareit.util.Constants.USER_ID_FROM_REQUEST;

@WebMvcTest(controllers = BookingController.class)
@AutoConfigureMockMvc
class BookingControllerTest {
    @MockBean
    BookingService bookingService;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    private final EasyRandom generator = new EasyRandom();
    private BookingResponseDto bookingResponseDto;

    @Test
    void getBookingsByBookerTest() throws Exception {
        List<BookingResponseDto> bookings = List.of(bookingResponseDto);

        when(bookingService.getBookingsByBooker(Mockito.anyLong(), Mockito.any(StateRequest.class), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(bookings);
        mockMvc.perform(get("/bookings")
                        .header(USER_ID_FROM_REQUEST, 1)
                        .accept(MediaType.ALL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
               // .andExpect(jsonPath("$.id", is("1"), Integer.class));

        verify(bookingService, times(1))
                .getBookingsByBooker(Mockito.anyLong(), Mockito.any(StateRequest.class), Mockito.anyInt(), Mockito.anyInt());
    }

    @BeforeEach
    void setUp() {
        ItemIdNameDto itemIdNameDto = new ItemIdNameDto() {
            private Long id = 1l;
            private String name = "ItemName";

            public Long getId() {
                return id;
            }

            public String getName() {
                return name;
            }
        };
        BookerDto bookerDto = new BookerDto() {
            private Long id = 1l;

            public Long getId() {
                return id;
            }
        };
        bookingResponseDto = BookingResponseDto.builder()
                .id(1l)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plus(1, ChronoUnit.DAYS))
                .item(itemIdNameDto)
                .booker(bookerDto)
                .status(Status.WAITING)
                .build();
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
                        .header(USER_ID_FROM_REQUEST, 1)
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