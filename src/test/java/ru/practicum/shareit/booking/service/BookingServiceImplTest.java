package ru.practicum.shareit.booking.service;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.dto.IncomingBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {
    @InjectMocks
    BookingServiceImpl bookingService;
    @Mock
    ItemRepository itemRepository;
    @Mock
    BookingRepository bookingRepository;

    @Mock
    private UserRepository userRepository;
    private final EasyRandom easyRandom = new EasyRandom();
    private IncomingBookingDto bookingDtoOne;
    private IncomingBookingDto bookingDtoTwo;
    private IncomingBookingDto incomingBookingDtoOne;
    private IncomingBookingDto incomingBookingDtoTwo;
    private User booker;
    private User owner;
    private Item item;
    private Booking booking;
    private BookingResponseDto bookingResponseDto;
    private int from = 0;
    private int size = 10;

    @Test
    void getBookingsByOwner() {
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