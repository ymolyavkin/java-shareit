package ru.practicum.shareit.booking.repository;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.IncomingBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.IncomingItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.dto.IncomingUserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@DataJpaTest
class BookingRepositoryTest {
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRequestRepository itemRequestRepository;
    private Booking bookingOne;
    private Booking bookingTwo;

    private final EasyRandom easyRandom = new EasyRandom();

    @BeforeEach
    private void addBookings() {
        IncomingBookingDto bookingDtoOne = new IncomingBookingDto();
        IncomingBookingDto bookingDtoTwo = new IncomingBookingDto();

        bookingDtoOne.setBookerId(2L);
        bookingDtoTwo.setBookerId(2L);
        bookingDtoOne.setItemId(1L);
        bookingDtoTwo.setItemId(1L);
        bookingDtoOne.setStatus(Status.APPROVED);
        bookingDtoTwo.setStatus(Status.APPROVED);

        bookingDtoOne.setStart(LocalDateTime.now().minus(2, ChronoUnit.DAYS));
        bookingDtoOne.setEnd(LocalDateTime.now().minus(1, ChronoUnit.DAYS));
        bookingDtoTwo.setStart(LocalDateTime.now().plus(1, ChronoUnit.HOURS));
        bookingDtoTwo.setEnd(LocalDateTime.now().plus(2, ChronoUnit.HOURS));

        IncomingUserDto userDto = easyRandom.nextObject(IncomingUserDto.class);
        userDto.setEmail("email@mail.ru");
        User user = UserMapper.mapToUser(userDto);
        user.setId(1L);

        IncomingItemDto itemDto = easyRandom.nextObject(IncomingItemDto.class);
        itemDto.setOwnerId(1L);
        itemDto.setRequestId(1L);
        Item item = ItemMapper.mapToItem(itemDto, user);
        item.setId(1L);

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(1L);
        itemRequest.setRequester(user);
        itemRequest.setCreated(LocalDateTime.now());
        itemRequest.setDescription("Description");

        bookingOne = BookingMapper.mapToBooking(bookingDtoOne, item, user);
        bookingTwo = BookingMapper.mapToBooking(bookingDtoTwo, item, user);

        userRepository.save(user);
        itemRequestRepository.save(itemRequest);
        itemRepository.save(item);

        bookingRepository.save(bookingOne);
        bookingRepository.save(bookingTwo);
    }

    @Test
    void findByItem_IdAndStartBeforeAndStatusOrderByStartDesc() {
    }

    @Test
    void findAllByBooker_Id() {
    }

    @Test
    void testFindAllByBooker_Id() {
    }

    @Test
    void findAllByBooker_IdAndStartBeforeAndEndAfter() {
    }

    @Test
    void findAllByBooker_IdAndEndBefore() {
    }

    @Test
    void findAllByBooker_IdAndStartAfter() {
    }

    @Test
    void findAllByBooker_IdAndStatus() {
    }

    @Test
    void findByBooker_IdOrderByStartDesc() {
    }

    @Test
    void existsByBooker_IdAndEndBeforeAndStatus() {
    }

    @Test
    void findByItem_Id() {
    }

    @Test
    void findLast() {
        Long itemId = 1L;
        LocalDateTime dateTimeNow = LocalDateTime.now();
        Booking actualBooking = bookingRepository.findLast(itemId, dateTimeNow);

        assertNotNull(actualBooking);
        assertEquals(actualBooking, bookingOne);
    }

    @Test
    void findNext() {
        Long itemId = 1L;
        LocalDateTime dateTimeNow = LocalDateTime.now();
        Booking actualBooking = bookingRepository.findNext(itemId, dateTimeNow);

        assertNotNull(actualBooking);
        assertEquals(actualBooking, bookingTwo);
    }

    @Test
    void findByItemIdAndBookerId() {
    }

    @Test
    void findByItem_IdInOrderByStartDesc() {
    }

    @Test
    void testFindByItem_IdInOrderByStartDesc() {
    }

    @Test
    void findFirstByItem_IdAndStartBeforeAndStatusOrderByStartDesc() {
    }

    @Test
    void findFirstByItem_IdAndStartAfterAndStatusOrderByStartAsc() {
    }

//    @AfterEach
//    void tearDown() {
//        bookingRepository.deleteAll();
//        itemRepository.deleteAll();
//        itemRequestRepository.deleteAll();
//        userRepository.deleteAll();
//    }
}