package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
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
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.dto.IncomingBookingDto;
import ru.practicum.shareit.booking.model.StateRequest;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.NoneXSharerUserIdException;
import ru.practicum.shareit.item.dto.ItemIdNameDto;
import ru.practicum.shareit.item.model.Item;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
    private IncomingBookingDto incomingBookingDto;
    private Item item;
    private final int start = 0;
    private final int size = 10;

    @BeforeEach
    void setUp() {
        incomingBookingDto = new IncomingBookingDto();
        incomingBookingDto.setItemId(1L);
        incomingBookingDto.setStart(LocalDateTime.now().plus(1, ChronoUnit.DAYS));
        incomingBookingDto.setEnd(LocalDateTime.now().plus(2, ChronoUnit.DAYS));

        incomingBookingDto.setBookerId(1L);
        ItemIdNameDto itemIdNameDto = new ItemIdNameDto() {
            private Long id = 1L;
            private String name = "ItemName";

            public Long getId() {
                return id;
            }

            public String getName() {
                return name;
            }
        };
        BookerDto bookerDto = new BookerDto() {
            private Long id = 1L;

            public Long getId() {
                return id;
            }
        };
        bookingResponseDto = BookingResponseDto.builder()
                .id(1L)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plus(1, ChronoUnit.DAYS))
                .item(itemIdNameDto)
                .booker(bookerDto)
                .status(Status.WAITING)
                .build();
    }

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

        verify(bookingService, times(1))
                .getBookingsByBooker(Mockito.anyLong(), Mockito.any(StateRequest.class), Mockito.anyInt(), Mockito.anyInt());
    }

    @Test
    void getBookingsByOwner() {
    }

    @Test
    void addBooking() throws Exception {
        when(bookingService.addBooking(Mockito.any(IncomingBookingDto.class)))
                .thenReturn(bookingResponseDto);

        mockMvc.perform(post("/bookings")
                        .header(USER_ID_FROM_REQUEST, 1)
                        .content(objectMapper.writeValueAsString(incomingBookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    void updateBooking() {
    }

    @Test
    void getBookingById() {
    }

    @SneakyThrows
    @Test
    void addBooking_whenUnknownUser_thenThrown() {
        Long userId = -1L;
        when(bookingService.addBooking(Mockito.any(IncomingBookingDto.class)))
                .thenReturn(bookingResponseDto);

        mockMvc.perform(post("/bookings")
                        .header(USER_ID_FROM_REQUEST, userId)
                        .content(objectMapper.writeValueAsString(incomingBookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @SneakyThrows
    @Test
    void createBookingWhenEndInPastValidationTest() {
//        Long userId = 1L;
//        Long itemId = 1L;
//        BookingShortDto createBookingDto = new BookingShortDto(userId, start, end.minusDays(7), itemId);
//        BookingDto bookingDto = new BookingDto(1L, start, end.minusDays(7), Status.WAITING, null, null);
//        when(bookingService.createBooking(anyLong(), any())).thenReturn(bookingDto);
//
//        mockMvc.perform(post("/bookings")
//                        .header("X-Sharer-User-Id", userId)
//                        .contentType("application/json")
//                        .content(objectMapper.writeValueAsString(createBookingDto)))
//                .andExpect(status().isBadRequest());
//
//        verify(bookingService, never()).createBooking(anyLong(), any());
    }

    @SneakyThrows
    @Test
    void createBookingWhenStartIsNullValidationTest() {
//        Long userId = 1L;
//        Long itemId = 1L;
//        BookingShortDto createBookingDto = new BookingShortDto(userId, null, end, itemId);
//        BookingDto bookingDto = new BookingDto(1L, null, end, Status.WAITING, null, null);
//        when(bookingService.createBooking(anyLong(), any())).thenReturn(bookingDto);
//
//        mockMvc.perform(post("/bookings")
//                        .header("X-Sharer-User-Id", userId)
//                        .contentType("application/json")
//                        .content(objectMapper.writeValueAsString(createBookingDto)))
//                .andExpect(status().isBadRequest());
//
//        verify(bookingService, never()).createBooking(anyLong(), any());
    }

    @SneakyThrows
    @Test
    void createBookingWhenEndIsNullValidationTest() {
//        Long userId = 1L;
//        Long itemId = 1L;
//        BookingShortDto createBookingDto = new BookingShortDto(userId, start, null, itemId);
//        BookingDto bookingDto = new BookingDto(1L, start, null, Status.WAITING, null, null);
//        when(bookingService.createBooking(anyLong(), any())).thenReturn(bookingDto);
//
//        mockMvc.perform(post("/bookings")
//                        .header("X-Sharer-User-Id", userId)
//                        .contentType("application/json")
//                        .content(objectMapper.writeValueAsString(createBookingDto)))
//                .andExpect(status().isBadRequest());
//
//        verify(bookingService, never()).createBooking(anyLong(), any());
    }

    @SneakyThrows
    @Test
    void getByIdTest() {
        Long bookingId = 1L;
        Long userId = 1L;
        //BookingDto bookingDto = new BookingDto(1L, start, end, Status.WAITING, null, null);
        when(bookingService.getBookingById(bookingId, userId)).thenReturn(bookingResponseDto);

        String result = mockMvc.perform(get("/bookings/{bookingId}", bookingId)
                        .header("X-Sharer-User-Id", 1))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(bookingResponseDto), result);
    }
    /*
    @GetMapping("/{bookingId}")
    public BookingResponseDto getBookingById(@RequestHeader(value = USER_ID_FROM_REQUEST, defaultValue = "-1") Long userId,
                                             @PathVariable Long bookingId) {
        log.info("Получен запрос на данных о бронировании с id = {}", bookingId);

        return bookingService.getBookingById(bookingId, userId);
    }
     */


    @SneakyThrows
    @Test
    void getAllByOwnerTest() {
//        BookingDto bookingDto = new BookingDto(1L, start, end, Status.WAITING, null, null);
//        when(bookingService.getAllOwnersBookingByState(anyLong(), any(), anyInt(),
//                anyInt())).thenReturn(List.of(bookingDto));
//        String result = mockMvc.perform(get("/bookings/owner")
//                        .header("X-Sharer-User-Id", 1)
//                        .param("state", "ALL")
//                        .param("from", String.valueOf(0))
//                        .param("size", String.valueOf(10)))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andReturn()
//                .getResponse()
//                .getContentAsString();
//
//        assertEquals(objectMapper.writeValueAsString(List.of(bookingDto)), result);
    }

    @SneakyThrows
    @Test
    void getAllBookingByStateTest() {
//        BookingDto bookingDto = new BookingDto(1L, start, end, Status.WAITING, null, null);
//        when(bookingService.getAllBookingByState(anyLong(), any(), anyInt(),
//                anyInt())).thenReturn(List.of(bookingDto));
//        String result = mockMvc.perform(get("/bookings")
//                        .header("X-Sharer-User-Id", 1)
//                        .param("state", "ALL")
//                        .param("from", String.valueOf(0))
//                        .param("size", String.valueOf(10)))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andReturn()
//                .getResponse()
//                .getContentAsString();
//
//        assertEquals(objectMapper.writeValueAsString(List.of(bookingDto)), result);
    }

    @SneakyThrows
    @Test
    void approveBookingTest() {
//        Long bookingId = 1L;
//        BookingDto bookingDto = new BookingDto(bookingId, start, end, Status.WAITING, null, null);
//        when(bookingService.approveBooking(anyLong(), anyLong(),
//                anyBoolean())).thenReturn(bookingDto);
//        String result = mockMvc.perform(patch("/bookings/{bookingId}", bookingId)
//                        .header("X-Sharer-User-Id", 1)
//                        .param("bookingId", "1L")
//                        .param("approved", "true"))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andReturn()
//                .getResponse()
//                .getContentAsString();
//
//        assertEquals(objectMapper.writeValueAsString(bookingDto), result);
    }
}