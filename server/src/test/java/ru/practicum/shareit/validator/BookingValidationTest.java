package ru.practicum.shareit.validator;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareitserver.booking.dto.BookingMapper;
import ru.practicum.shareitserver.booking.dto.IncomingBookingDto;
import ru.practicum.shareitserver.booking.model.Booking;
import ru.practicum.shareitserver.exception.BadRequestException;
import ru.practicum.shareitserver.item.model.Item;
import ru.practicum.shareitserver.user.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;
import static ru.practicum.shareitserver.validator.BookingValidation.bookingIsValid;

class BookingValidationTest {
    private IncomingBookingDto incomingBookingDto;
    private User user;
    private Item item;
    private Item itemOther;
    private Booking booking;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private final LocalDateTime startPast = LocalDateTime.parse("2023-01-14 11:04", formatter);
    private final LocalDateTime endPast = LocalDateTime.parse("2023-01-15 11:04", formatter);
    private final LocalDateTime startAfter = LocalDateTime.parse("2023-01-16 11:04", formatter);
    private final LocalDateTime endBefore = LocalDateTime.parse("2023-01-15 11:04", formatter);
    private final LocalDateTime startValid = LocalDateTime.now().plusDays(1);
    private final LocalDateTime endValid = LocalDateTime.now().plusDays(2);


    @BeforeEach
    void setUp() {
        Long bookerId = 1L;
        Long itemId = 1L;
        Long bookingId = 1L;
        incomingBookingDto = new IncomingBookingDto();
        incomingBookingDto.setBookerId(bookerId);
        incomingBookingDto.setItemId(itemId);
        incomingBookingDto.setStart(startValid);
        incomingBookingDto.setEnd(endValid);
        item = new Item(1L, "ItemName", "DescriptionItem", true, user, 1L, 0);
        itemOther = new Item(2L, "ItemNameOther", "DescriptionItemOther", false, user, 1L, 0);
        user = new User();
        user.setEmail("user@email.ru");
        user.setName("userName");
        user.setId(1L);
        booking = BookingMapper.mapToBooking(incomingBookingDto, item, user);
        booking.setId(bookingId);
    }

    @AfterEach
    void tearDown() {
        item.setAvailable(true);
        incomingBookingDto.setStart(startValid);
        incomingBookingDto.setEnd(endValid);
    }

    @Test
    void bookingIsValid_thenNotThrown() {
        assertDoesNotThrow(() -> bookingIsValid(incomingBookingDto, item));
        bookingIsValid(incomingBookingDto, item);
    }

    @Test
    void bookingIsValid_AvailableFalse_thenThrown() {
        item.setAvailable(false);
        Throwable thrown = assertThrows(BadRequestException.class, () -> {
            bookingIsValid(incomingBookingDto, item);
        });
        assertNotNull(thrown.getMessage());
    }

    @Test
    void bookingIsValid_getStartIsNull_thenThrown() {
        incomingBookingDto.setStart(null);
        Throwable thrown = assertThrows(BadRequestException.class, () -> {
            bookingIsValid(incomingBookingDto, item);
        });
        assertNotNull(thrown.getMessage());
    }

    @Test
    void bookingIsValid_getEndIsNull_thenThrown() {
        incomingBookingDto.setEnd(null);
        Throwable thrown = assertThrows(BadRequestException.class, () -> {
            bookingIsValid(incomingBookingDto, item);
        });
        assertNotNull(thrown.getMessage());
    }

    @Test
    void bookingIsValid_whenEndBeforeStart_thenThrown() {
        incomingBookingDto.setStart(startAfter);
        incomingBookingDto.setEnd(endBefore);
        Throwable thrown = assertThrows(BadRequestException.class, () -> {
            bookingIsValid(incomingBookingDto, item);
        });
        assertNotNull(thrown.getMessage());
    }

    @Test
    void bookingIsValid_whenStartBeforeNow_thenThrown() {
        incomingBookingDto.setStart(startPast);

        Throwable thrown = assertThrows(BadRequestException.class, () -> {
            bookingIsValid(incomingBookingDto, item);
        });
        assertNotNull(thrown.getMessage());
    }

    @Test
    void bookingIsValid_whenEndBeforeNow_thenThrown() {
        incomingBookingDto.setEnd(endPast);

        Throwable thrown = assertThrows(BadRequestException.class, () -> {
            bookingIsValid(incomingBookingDto, item);
        });
        assertNotNull(thrown.getMessage());
    }

    @Test
    void bookingIsValid_whenEndEqualStart_thenThrown() {
        incomingBookingDto.setStart(endPast);
        incomingBookingDto.setEnd(endPast);

        Throwable thrown = assertThrows(BadRequestException.class, () -> {
            bookingIsValid(incomingBookingDto, item);
        });
        assertNotNull(thrown.getMessage());
        assertEquals(incomingBookingDto.getEnd(), incomingBookingDto.getStart());
    }
}