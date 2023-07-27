package ru.practicum.shareit.booking.service;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.dto.IncomingBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.IncomingItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.user.dto.IncomingUserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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

    void setUp() {
        bookingService = new BookingServiceImpl(itemRepository, userRepository, bookingRepository);
        bookingDtoOne = new IncomingBookingDto();
        bookingDtoTwo = new IncomingBookingDto();

        bookingDtoOne.setBookerId(2L);
        bookingDtoTwo.setBookerId(2L);
        bookingDtoOne.setItemId(1L);
        bookingDtoTwo.setItemId(1L);

        bookingDtoOne.setStart(LocalDateTime.now().plus(1, ChronoUnit.DAYS));
        bookingDtoOne.setEnd(LocalDateTime.now().plus(2, ChronoUnit.DAYS));
        bookingDtoTwo.setStart(LocalDateTime.now().plus(3, ChronoUnit.DAYS));
        bookingDtoTwo.setEnd(LocalDateTime.now().plus(4, ChronoUnit.DAYS));


        IncomingUserDto userDtoOne = easyRandom.nextObject(IncomingUserDto.class);
        userDtoOne.setEmail("email@mail.ru");
        User userOne = userRepository.save(UserMapper.mapToUser(userDtoOne));
        //User userOne =UserMapper.mapToUser(userDtoOne);
        userOne.setId(1L);
        IncomingUserDto userDtoTwo = easyRandom.nextObject(IncomingUserDto.class);
        userDtoTwo.setEmail("mail@yandex.ru");


        User userTwo = userRepository.save(UserMapper.mapToUser(userDtoTwo));
        //  User userTwo =UserMapper.mapToUser(userDtoTwo);
        userTwo.setId(2L);
        IncomingItemDto itemDto = easyRandom.nextObject(IncomingItemDto.class);
        itemDto.setOwnerId(1L);

        Item item = itemRepository.save(ItemMapper.mapToItem(itemDto, userOne));

    }

    @AfterEach
    void tearDown() {
        bookingRepository.deleteAll();
        itemRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DirtiesContext
    void getAll() {
        setUp();
        BookingResponseDto bookingResponseDtoOne = bookingService.addBooking(bookingDtoOne);
        BookingResponseDto bookingResponseDtoTwo = bookingService.addBooking(bookingDtoTwo);
        List<Booking> bookings = bookingRepository.findAll();
        assertNotNull(bookings);
        assertEquals(2, bookings.size());
    }

    @Test
    @DirtiesContext
    void getBookingById() {
        setUp();
        BookingResponseDto bookingResponseDtoOne = bookingService.addBooking(bookingDtoOne);
        BookingResponseDto bookingResponseDtoTwo = bookingService.addBooking(bookingDtoTwo);
        List<Booking> bookings = bookingRepository.findAll();
        assertNotNull(bookings);
        assertEquals(2, bookings.size());
    }

    @Test
    @DirtiesContext
    void getBookingsByOwner() {
    }

    @Test
    @DirtiesContext
    void getBookingsByBooker() {
    }

    @Test
    @DirtiesContext
    void addBooking() {
    }

    @Test
    @DirtiesContext
    void isOverlapTime() {
    }

    @Test
    @DirtiesContext
    void updateBooking() {
    }

    @Test
    @DirtiesContext
    void approvingBooking() {
    }
}