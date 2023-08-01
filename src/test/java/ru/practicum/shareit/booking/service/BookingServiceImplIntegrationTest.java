package ru.practicum.shareit.booking.service;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.dto.IncomingBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.StateRequest;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.UnsupportedStatusException;
import ru.practicum.shareit.item.dto.IncomingItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.IncomingItemRequestDto;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.dto.IncomingUserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class BookingServiceImplIntegrationTest {
    @Autowired
    BookingService bookingService;
    //@Autowired
//@InjectMocks
//BookingServiceImpl bookingService;
    @MockBean
    ItemRepository itemRepository;
    //@Autowired
    @MockBean
    ItemRequestRepository itemRequestRepository;
    //@Autowired
    @MockBean
    UserRepository userRepository;
    //@Autowired
    @MockBean
    BookingRepository bookingRepository;
    private final EasyRandom easyRandom = new EasyRandom();
    private IncomingBookingDto incomingBookingDtoOne;
    private IncomingBookingDto incomingBookingDtoTwo;
    private BookingResponseDto bookingResponseDto;
    private Booking booking;
    private Item item;
    private User user;

    @BeforeEach
    void setUp() {
        bookingService = new BookingServiceImpl(itemRepository, userRepository, bookingRepository);
        incomingBookingDtoOne = new IncomingBookingDto();
        incomingBookingDtoTwo = new IncomingBookingDto();

        incomingBookingDtoOne.setBookerId(1L);
        incomingBookingDtoTwo.setBookerId(2L);
        incomingBookingDtoOne.setItemId(1L);
        incomingBookingDtoTwo.setItemId(1L);

        incomingBookingDtoOne.setStart(LocalDateTime.now().plus(1, ChronoUnit.DAYS));
        incomingBookingDtoOne.setEnd(LocalDateTime.now().plus(2, ChronoUnit.DAYS));
        incomingBookingDtoTwo.setStart(LocalDateTime.now().plus(3, ChronoUnit.DAYS));
        incomingBookingDtoTwo.setEnd(LocalDateTime.now().plus(4, ChronoUnit.DAYS));

        IncomingUserDto userDtoOne = easyRandom.nextObject(IncomingUserDto.class);
        userDtoOne.setEmail("email@mail.ru");
        // user = userRepository.save(UserMapper.mapToUser(userDtoOne));
        user = UserMapper.mapToUser(userDtoOne);
        user.setId(1L);
        IncomingUserDto userDtoTwo = easyRandom.nextObject(IncomingUserDto.class);
        userDtoTwo.setEmail("mail@yandex.ru");


        // User userTwo = userRepository.save(UserMapper.mapToUser(userDtoTwo));
        //   userTwo.setId(2L);
        IncomingItemDto itemDto = easyRandom.nextObject(IncomingItemDto.class);
        itemDto.setOwnerId(1L);
        itemDto.setRequestId(1L);

        IncomingItemRequestDto incomingItemRequestDto = new IncomingItemRequestDto();
        incomingItemRequestDto.setDescription("Description");

        // ItemRequest itemRequest = itemRequestRepository.save(ItemRequestMapper.mapToItemRequest(incomingItemRequestDto, userOne));

        item = ItemMapper.mapToItem(itemDto, user);
        item.setId(1L);
        booking = BookingMapper.mapToBooking(incomingBookingDtoOne, item, user);
        booking.setId(1L);
        bookingResponseDto = BookingMapper.mapToBookingResponseDto(booking, item);
        bookingResponseDto.setId(1L);
    }

    @Test
    @DirtiesContext
    void getAllTest() {
        BookingResponseDto bookingResponseDtoOne = bookingService.addBooking(incomingBookingDtoOne);
        BookingResponseDto bookingResponseDtoTwo = bookingService.addBooking(incomingBookingDtoTwo);
        List<Booking> bookings = bookingRepository.findAll();
        assertNotNull(bookings);
        assertEquals(2, bookings.size());
    }

    @Test
    @DirtiesContext
    void getBookingByIdTest() {
        BookingResponseDto bookingResponseDtoOne = bookingService.addBooking(incomingBookingDtoOne);
        BookingResponseDto bookingResponseDtoTwo = bookingService.addBooking(incomingBookingDtoTwo);
        List<Booking> bookings = bookingRepository.findAll();
        assertNotNull(bookings);
        assertEquals(2, bookings.size());
    }

    @Test
    @DirtiesContext
    void getBookingsByOwnerTest() {
        BookingResponseDto bookingResponseDtoOne = bookingService.addBooking(incomingBookingDtoOne);
        BookingResponseDto bookingResponseDtoTwo = bookingService.addBooking(incomingBookingDtoTwo);
    }

    @ParameterizedTest
    @MethodSource("ru.practicum.shareit.util.TestData#argsProviderFactoryBookingsByBooker")
    void getBookingsByBookerTest(Long bookerId, StateRequest state) {
        List<Booking> expectedListBookings;
        List<BookingResponseDto> result;

        int from = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(from / size, size, Sort.by("id").descending());
        Page<Booking> bookingPage;
        if (bookerId.equals(99L)) {
            when(userRepository.findById(bookerId))
                    .thenThrow(new NotFoundException(String.format("Пользователь с id %d не найден", bookerId)));
            assertThrows(NotFoundException.class, () -> bookingService.getBookingsByBooker(bookerId, state, from, size));
        } else {
            List<BookingResponseDto> expected;
            switch (state) {
                case UNSUPPORTED_STATUS:
                    when(userRepository.findById(bookerId)).thenReturn(Optional.of(user));

                    assertThrows(UnsupportedStatusException.class, () -> bookingService.getBookingsByBooker(bookerId, state, from, size));
                    break;
                case ALL:
                    expectedListBookings = List.of(booking);
                    bookingPage = new PageImpl<>(expectedListBookings);
                    when(userRepository.findById(bookerId)).thenReturn(Optional.of(user));
                    when(bookingRepository.findAllByBooker_Id(bookerId, pageable)).thenReturn(bookingPage);
                    expected = bookingPage.getContent()
                            .stream()
                            .map(bookingResponse -> BookingMapper.mapToBookingResponseDto(bookingResponse, item))
                            .collect(Collectors.toList());
                    result = bookingService.getBookingsByBooker(bookerId, state, from, size);

                    assertEquals(expected, result);
                    break;
//            case CURRENT:
//                bookings = bookingRepository.findAllByBooker_IdAndStartBeforeAndEndAfter(bookerId, dateTimeNow, dateTimeNow, SORT_BY_DESC);
//                break;
//            case FUTURE:
//                bookings = bookingRepository.findAllByBooker_IdAndStartAfter(bookerId, dateTimeNow, SORT_BY_DESC);
//                break;
//            case PAST:
//                bookings = bookingRepository.findAllByBooker_IdAndEndBefore(bookerId, dateTimeNow, SORT_BY_DESC);
//                break;
//            case WAITING:
//                bookings = bookingRepository.findAllByBooker_IdAndStatus(bookerId, Status.WAITING, SORT_BY_DESC);
//                break;
//            case REJECTED:
//                when(userRepository.findById(bookerId)).thenReturn(Optional.of(user));
//                when(bookingRepository.findAllByBooker_Id(bookerId, pageable)).thenReturn(bookingPage);
//                expected = bookingPage.getContent()
//                        .stream()
//                        .map(bookingResponse -> BookingMapper.mapToBookingResponseDto(bookingResponse, item))
//                        .collect(Collectors.toList());
//                result = bookingService.getBookingsByBooker(bookerId, state, from, size);
//
//                assertEquals(expected, result);
//                    break;
                default:
//                when(bookingRepository.findAllByBooker_Id(bookerId, pageable)).thenReturn(bookingPage);
//
//                result = bookingService.getBookingsByBooker(bookerId, state, from, size);
//
//                assertEquals(bookingPage.getContent(), result);
            }
//        ItemLastNextDto itemResponse = itemService.getItemById(1L, 1L);
//        assertEquals(1L, itemResponse.getId());
//        BookingResponseDto bookingResponseDtoOne = bookingService.addBooking(bookingDtoOne);
//        BookingResponseDto bookingResponseDtoTwo = bookingService.addBooking(bookingDtoTwo);
//        List<Booking> bookings = bookingRepository.findAll();
//        assertNotNull(bookings);
//        assertEquals(2, bookings.size());
        }
    }
    /*
     @Transactional(readOnly = true)
    @Override
    public List<BookingResponseDto> getBookingsByBooker(Long bookerId, StateRequest state, Integer from, Integer size) {
        LocalDateTime dateTimeNow = LocalDateTime.now();
        User booker = userRepository.findById(bookerId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с id %d не найден", bookerId)));
        if (state == StateRequest.UNSUPPORTED_STATUS) {
            throw new UnsupportedStatusException("Unknown state: UNSUPPORTED_STATUS");
        }
        List<Booking> bookings;
        switch (state) {
            case ALL:
                Pageable pageable = PageRequest.of(from / size, size, Sort.by("id").descending());
                Page<Booking> bookingPage = bookingRepository.findAllByBooker_Id(bookerId, pageable);

                bookings = bookingPage.getContent();
                break;
            case CURRENT:
                bookings = bookingRepository.findAllByBooker_IdAndStartBeforeAndEndAfter(bookerId, dateTimeNow, dateTimeNow, SORT_BY_DESC);
                break;
            case FUTURE:
                bookings = bookingRepository.findAllByBooker_IdAndStartAfter(bookerId, dateTimeNow, SORT_BY_DESC);
                break;
            case PAST:
                bookings = bookingRepository.findAllByBooker_IdAndEndBefore(bookerId, dateTimeNow, SORT_BY_DESC);
                break;
            case WAITING:
                bookings = bookingRepository.findAllByBooker_IdAndStatus(bookerId, Status.WAITING, SORT_BY_DESC);
                break;
            case REJECTED:
                bookings = bookingRepository.findAllByBooker_IdAndStatus(bookerId, Status.REJECTED, SORT_BY_DESC);
                break;
            default:
                bookings = Collections.emptyList();
        }
        return bookings
                .stream()
                .map(booking -> BookingMapper.mapToBookingResponseDto(booking, booking.getItem()))
                .collect(Collectors.toList());
    }
     */

    @Test
    @DirtiesContext
    void addBookingTest() {
    }

    @Test
    @DirtiesContext
    void isOverlapTimeTest() {
    }

    @Test
    @DirtiesContext
    void updateBookingTest() {
    }

    @Test
    @DirtiesContext
    void approvingBookingTest() {
    }
}