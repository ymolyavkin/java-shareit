package ru.practicum.shareit.booking.service;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.dto.IncomingBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class BookingServiceImplIntegrationTest {
    @Autowired
    BookingService bookingService;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    BookingRepository bookingRepository;
    private final EasyRandom easyRandom = new EasyRandom();
    private IncomingBookingDto bookingDtoOne;
    private IncomingBookingDto bookingDtoTwo;
    @BeforeEach
    void setUp() {
        bookingService = new BookingServiceImpl(itemRepository, userRepository, bookingRepository);
        bookingDtoOne = easyRandom.nextObject(IncomingBookingDto.class);
        bookingDtoTwo = easyRandom.nextObject(IncomingBookingDto.class);

        /*bookingOne.setId(1L);
        bookingTwo.setId(2L);*/
        /*bookingRepository.save(bookingOne);
        bookingRepository.save(bookingTwo);*/
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getAll() {
        BookingResponseDto bookingResponseDtoOne = bookingService.addBooking(bookingDtoOne);
        BookingResponseDto bookingResponseDtoTwo = bookingService.addBooking(bookingDtoTwo);
        List<Booking> bookings = bookingRepository.findAll();
        assertNotNull(bookings);
        assertEquals(2, bookings.size());
    }

    @Test
    void getBookingById() {
    }

    @Test
    void getBookingsByOwner() {
    }

    @Test
    void getBookingsByBooker() {
    }

    @Test
    void addBooking() {
    }

    @Test
    void isOverlapTime() {
    }

    @Test
    void updateBooking() {
    }

    @Test
    void approvingBooking() {
    }
}