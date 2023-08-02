package ru.practicum.shareit.booking.service;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.dto.IncomingUserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@SpringBootTest
class BookingServiceImplIntegrationTest {
    @Autowired
    BookingService bookingService;
    //@Autowired
    @Mock
    ItemRepository itemRepository;
    //@Autowired
    @Mock
    ItemRequestRepository itemRequestRepository;
    //@Autowired
    @Mock
    UserRepository userRepository;
    @Mock
    BookingRepository bookingRepository;
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


    @BeforeEach
    void setUp() {
        Long bookerId = 1L;
        Long otherBookerId = 2L;
        Long ownerId = 3L;
        Long itemId = 1L;
        Long requestId = 1L;
        Long bookingId = 1L;
        bookingService = new BookingServiceImpl(itemRepository, userRepository, bookingRepository);

        incomingBookingDtoOne = new IncomingBookingDto();
        incomingBookingDtoTwo = new IncomingBookingDto();

        incomingBookingDtoOne.setBookerId(bookerId);
        incomingBookingDtoTwo.setBookerId(otherBookerId);
        incomingBookingDtoOne.setItemId(itemId);
        incomingBookingDtoTwo.setItemId(itemId);

        incomingBookingDtoOne.setStart(LocalDateTime.now().plus(1, ChronoUnit.DAYS));
        incomingBookingDtoOne.setEnd(LocalDateTime.now().plus(2, ChronoUnit.DAYS));
        incomingBookingDtoTwo.setStart(LocalDateTime.now().plus(3, ChronoUnit.DAYS));
        incomingBookingDtoTwo.setEnd(LocalDateTime.now().plus(4, ChronoUnit.DAYS));

        IncomingUserDto userDtoOne = easyRandom.nextObject(IncomingUserDto.class);
        userDtoOne.setEmail("email@mail.ru");

        booker = UserMapper.mapToUser(userDtoOne);
        booker.setId(bookerId);
        IncomingUserDto userDtoTwo = easyRandom.nextObject(IncomingUserDto.class);
        userDtoTwo.setEmail("mail@yandex.ru");

        owner = UserMapper.mapToUser(userDtoTwo);
        owner.setId(ownerId);
        IncomingItemDto itemDto = easyRandom.nextObject(IncomingItemDto.class);
        itemDto.setOwnerId(ownerId);
        itemDto.setRequestId(requestId);

        IncomingItemRequestDto incomingItemRequestDto = new IncomingItemRequestDto();
        incomingItemRequestDto.setDescription("Description");

        item = ItemMapper.mapToItem(itemDto, booker);
        item.setId(itemId);
        booking = BookingMapper.mapToBooking(incomingBookingDtoOne, item, booker);
        booking.setId(bookingId);
        bookingResponseDto = BookingMapper.mapToBookingResponseDto(booking, item);
    }

    // @BeforeEach
    void setUp1() {
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
        userOne.setId(1L);
        IncomingUserDto userDtoTwo = easyRandom.nextObject(IncomingUserDto.class);
        userDtoTwo.setEmail("mail@yandex.ru");


        User userTwo = userRepository.save(UserMapper.mapToUser(userDtoTwo));
        userTwo.setId(2L);
        IncomingItemDto itemDto = easyRandom.nextObject(IncomingItemDto.class);
        itemDto.setOwnerId(1L);
        itemDto.setRequestId(1L);

        IncomingItemRequestDto incomingItemRequestDto = new IncomingItemRequestDto();
        incomingItemRequestDto.setDescription("Description");

        ItemRequest itemRequest = itemRequestRepository.save(ItemRequestMapper.mapToItemRequest(incomingItemRequestDto, userOne));

        Item item = itemRepository.save(ItemMapper.mapToItem(itemDto, userOne));
    }

    @Test
    @DirtiesContext
    void getAllTest() {
        List<Booking> expectedListBookings = Collections.emptyList();
        when(bookingRepository.findAll())
                .thenReturn(expectedListBookings);
        List<Booking> actualListBooking = bookingService.getAll();

        assertEquals(expectedListBookings, actualListBooking);
    }

    /*
    @ParameterizedTest
    @CsvSource({
        "alex, 30, HR, Active",
        "brian, 35, Technology, Active",
        "charles, 40, Finance, Purged"
    })
    void testWithCsvSource(String name, int age, String department, String status)
     */
    @DirtiesContext
    @ParameterizedTest
    @CsvSource({
            "1, 1, 1",
            "1, 99, 1"
    })
    void getBookingByIdTest(long bookingId, long userId, long itemId) {
        System.out.println();
        if (bookingId != booking.getId()) {
            Mockito.when(bookingRepository.getReferenceById(bookingId))
                    .thenThrow(new NotFoundException(String.format("Бронирование с id %d не найдено", bookingId)));
            assertThrows(NotFoundException.class, () -> bookingService.getBookingById(bookingId, userId));
        } else if (itemId != item.getId()) {
            Mockito.when(itemRepository.findById(itemId)).thenReturn(Optional.empty());
            doThrow(NotFoundException.class).when(itemRepository).findById(itemId);

            assertThrows(NotFoundException.class, () -> bookingService.getBookingById(bookingId, userId));
//            Mockito.when(itemRepository.findById(itemId))
//                    .thenThrow(new NotFoundException(String.format("Вещь с id %d не найдена", itemId)));
//            assertThrows(NotFoundException.class, () -> bookingService.getBookingById(bookingId, userId));
        }
        if (userId != 1 && userId != 3) {
            Mockito.when(bookingRepository.getReferenceById(bookingId)).thenReturn(booking);
            Mockito.when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
            Throwable thrown = assertThrows(NotFoundException.class, () -> {
                bookingService.getBookingById(bookingId, userId);
            });
            assertNotNull(thrown.getMessage());


            // assertEquals("Пользователь с id 99 не не автор бронирования и не владелец вещи, данные о бронировании недоступны", exception.getMessage());
        } else {
            Mockito.when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
            Mockito.when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

            Mockito.when(bookingRepository.getReferenceById(booking.getId())).thenReturn(booking);
            BookingResponseDto actual = bookingService.getBookingById(bookingId, userId);
            BookingResponseDto expected = BookingMapper.mapToBookingResponseDto(booking, item);
            assertEquals(expected, actual);
        }
    }

    /*
    public BookingResponseDto getBookingById(Long id, Long userId) {
            Booking booking = bookingRepository.getReferenceById(id);
            Item item = itemRepository.findById(booking.getItemId())
                    .orElseThrow(() -> new NotFoundException(String.format("Вещь с id %d не найдена", booking.getItemId())));
            if (!booking.getBookerId().equals(userId) && !item.getOwnerId().equals(userId)) {
                throw new NotFoundException(String.format("Пользователь с id %d не не автор бронирования и не владелец вещи, данные о бронировании недоступны", userId));
            }

            return BookingMapper.mapToBookingResponseDto(booking, item);
        }
     */
    @DirtiesContext
    @ParameterizedTest
    @MethodSource("ru.practicum.shareit.util.TestData#argsProviderFactoryBookingsByOwner")
    void getBookingsByOwnerParameterTest(Long ownerId, StateRequest state) {
        List<Booking> expectedListBookings;
        List<BookingResponseDto> result;

        Pageable pageable = PageRequest.of(from / size, size, Sort.by("id").descending());
        Page<Booking> bookingPage;
        if (ownerId.equals(99L)) {
            Mockito.when(userRepository.findById(ownerId)).thenThrow(new NotFoundException(String.format("Пользователь с id %d не найден", ownerId)));
            assertThrows(NotFoundException.class, () -> bookingService.getBookingsByOwner(ownerId, state, from, size));
        } else {
//            List<BookingResponseDto> expected;
//            List<Long> itemIdsByOwner = List.of(ownerId);
//            expectedListBookings = Collections.emptyList();
//
//            bookingPage = new PageImpl<>(expectedListBookings);
//
//            List<Booking> bookingList = bookingPage.getContent();
//            Mockito.when(userRepository.findById(ownerId)).thenReturn(Optional.of(booker));
//            Mockito.when(bookingRepository.findByItem_IdInOrderByStartDesc(itemIdsByOwner, pageable)).thenReturn(bookingPage);
//            expected = bookingList.stream().map(bookingResponse -> BookingMapper.mapToBookingResponseDto(bookingResponse, item)).collect(Collectors.toList());
//            result = bookingService.getBookingsByOwner(ownerId, state, FROM, SIZE);
//
//            assertEquals(expected, result);
            // List<Booking> bookings;
            switch (state) {
                case UNSUPPORTED_STATUS:
                    Mockito.when(userRepository.findById(ownerId)).thenReturn(Optional.of(booker));
                    assertThrows(UnsupportedStatusException.class, () -> bookingService.getBookingsByOwner(ownerId, state, from, size));
                    break;
                case ALL:
                case CURRENT:
                case FUTURE:
                case PAST:
                case WAITING:
                case REJECTED:
                    List<BookingResponseDto> expected;
                    List<Long> itemIdsByOwner = List.of(ownerId);
                    expectedListBookings = Collections.emptyList();

                    bookingPage = new PageImpl<>(expectedListBookings);

                    List<Booking> bookingList = bookingPage.getContent();
                    Mockito.when(userRepository.findById(ownerId)).thenReturn(Optional.of(booker));
                    Mockito.when(bookingRepository.findByItem_IdInOrderByStartDesc(itemIdsByOwner, pageable)).thenReturn(bookingPage);
                    expected = bookingList
                            .stream()
                            .map(bookingResponse -> BookingMapper.mapToBookingResponseDto(bookingResponse, item))
                            .collect(Collectors.toList());
                    result = bookingService.getBookingsByOwner(ownerId, state, from, size);

                    assertEquals(expected, result);
                default:
                    result = Collections.emptyList();
                    assertEquals(0, result.size());
            }
        }
    }

    @DirtiesContext
    @ParameterizedTest
    @MethodSource("ru.practicum.shareit.util.TestData#argsProviderFactoryBookingsByBooker")
    void getBookingsByBookerParameterTest(Long bookerId, StateRequest state) {
        List<Booking> expectedListBookings;
        List<BookingResponseDto> result;

        int from = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(from / size, size, Sort.by("id").descending());
        Page<Booking> bookingPage;
        if (bookerId.equals(99L)) {
            Mockito.when(userRepository.findById(bookerId)).thenThrow(new NotFoundException(String.format("Пользователь с id %d не найден", bookerId)));
            assertThrows(NotFoundException.class, () -> bookingService.getBookingsByBooker(bookerId, state, from, size));
        } else {
            List<BookingResponseDto> expected;
            switch (state) {
                case UNSUPPORTED_STATUS:
                    Mockito.when(userRepository.findById(bookerId)).thenReturn(Optional.of(booker));

                    assertThrows(UnsupportedStatusException.class, () -> bookingService.getBookingsByBooker(bookerId, state, from, size));
                    break;
                case ALL:
                    expectedListBookings = List.of(booking);
                    bookingPage = new PageImpl<>(expectedListBookings);
                    Mockito.when(userRepository.findById(bookerId)).thenReturn(Optional.of(booker));
                    Mockito.when(bookingRepository.findAllByBooker_Id(bookerId, pageable)).thenReturn(bookingPage);
                    expected = bookingPage.getContent()
                            .stream()
                            .map(bookingResponse -> BookingMapper.mapToBookingResponseDto(bookingResponse, item))
                            .collect(Collectors.toList());
                    result = bookingService.getBookingsByBooker(bookerId, state, from, size);

                    assertEquals(expected, result);
                    break;
                case CURRENT:
                case FUTURE:
                case PAST:
                case WAITING:
                case REJECTED:
                    //default:
                    expectedListBookings = Collections.emptyList();
                    bookingPage = new PageImpl<>(expectedListBookings);
                    Mockito.when(userRepository.findById(bookerId)).thenReturn(Optional.of(booker));
                    Mockito.when(bookingRepository.findAllByBooker_Id(bookerId, pageable)).thenReturn(bookingPage);
                    expected = bookingPage.getContent()
                            .stream()
                            .map(bookingResponse -> BookingMapper.mapToBookingResponseDto(bookingResponse, item))
                            .collect(Collectors.toList());
                    result = bookingService.getBookingsByBooker(bookerId, state, from, size);

                    assertEquals(expected, result);
                    break;
                default:
                    result = Collections.emptyList();
                    assertEquals(0, result.size());
            }
        }
    }

    @DirtiesContext
    @ParameterizedTest
    @ValueSource(ints = {1, -1, 99})
    void addBookingTest(int bookerId) {
        item.setOwner(owner);
        //bookingRepository.save(booking);
        // userRepository.save(booker);
        switch (bookerId) {
            case 1:
//                System.out.println(item);
//                System.out.println();
//                Mockito.when(userRepository.findById((long) bookerId)).thenReturn(Optional.of(booker));
//                Mockito.when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
//                BookingResponseDto actualBooking = bookingService.addBooking(incomingBookingDtoOne);
                break;
            case -1:
                Mockito.when(userRepository.findById((long) bookerId)).thenReturn(Optional.of(booker));
                doThrow(NotFoundException.class).when(userRepository).findById((long) bookerId);
//                Throwable thrown = assertThrows(NoneXSharerUserIdException.class, () -> {
//                    bookingService.addBooking(incomingBookingDtoOne);
//                });
//                assertNotNull(thrown.getMessage());
                break;
            default:
                Mockito.when(userRepository.findById((long) bookerId)).thenReturn(Optional.empty());
                doThrow(NotFoundException.class).when(userRepository).findById((long) bookerId);

                assertThrows(NotFoundException.class, () -> bookingService.addBooking(incomingBookingDtoOne));
        }

    }
    /*
    public BookingResponseDto addBooking(IncomingBookingDto incomingBookingDto) {
        if (incomingBookingDto.getBookerId().equals(-1L)) {
            throw new NoneXSharerUserIdException("Не указан инициатор бронирования");
        }
        Long bookerId = incomingBookingDto.getBookerId();
        Long itemId = incomingBookingDto.getItemId();
        User booker = userRepository.findById(bookerId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с id %d не найден", bookerId)));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(String.format("Вещь с id %d не найдена", itemId)));
        if (bookerId.equals(item.getOwnerId())) {
            throw new NotFoundException("Инициатор бронирования и владелец запрашиваемой вещи совпадают");
        }
        BookingValidation.bookingIsValid(incomingBookingDto, item);
        Booking resultBooking = BookingMapper.mapToBooking(incomingBookingDto, item, booker);
        List<Booking> allBookingsByItem = bookingRepository.findByItem_Id(itemId);

        boolean isOverlap = allBookingsByItem
                .stream()
                .map(booking -> isOverlapTime(booking, resultBooking))
                .reduce(Boolean::logicalOr).orElse(false);

        if (isOverlap) {
            throw new NotFoundException("Данная вещь на этот период недоступна");
        }
        Booking booking = bookingRepository.save(resultBooking);

        return BookingMapper.mapToBookingResponseDto(booking, item);
    }
     */

    @Test
    @DirtiesContext
    void updateBookingTest() {
    }

    @Test
    @DirtiesContext
    void approvingBookingTest() {
    }
}
//package ru.practicum.shareit.booking.service;
//
//import org.jeasy.random.EasyRandom;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.MethodSource;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.data.domain.*;
//import org.springframework.test.annotation.DirtiesContext;
//import ru.practicum.shareit.booking.dto.BookingMapper;
//import ru.practicum.shareit.booking.dto.BookingResponseDto;
//import ru.practicum.shareit.booking.dto.IncomingBookingDto;
//import ru.practicum.shareit.booking.model.Booking;
//import ru.practicum.shareit.booking.model.StateRequest;
//import ru.practicum.shareit.booking.repository.BookingRepository;
//import ru.practicum.shareit.exception.NotFoundException;
//import ru.practicum.shareit.exception.UnsupportedStatusException;
//import ru.practicum.shareit.item.dto.IncomingItemDto;
//import ru.practicum.shareit.item.dto.ItemMapper;
//import ru.practicum.shareit.item.model.Item;
//import ru.practicum.shareit.item.repository.ItemRepository;
//import ru.practicum.shareit.request.dto.IncomingItemRequestDto;
//import ru.practicum.shareit.request.repository.ItemRequestRepository;
//import ru.practicum.shareit.user.dto.IncomingUserDto;
//import ru.practicum.shareit.user.dto.UserMapper;
//import ru.practicum.shareit.user.model.User;
//import ru.practicum.shareit.user.repository.UserRepository;
//
//import java.time.LocalDateTime;
//import java.time.temporal.ChronoUnit;
//import java.util.Collections;
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.when;
//
//@SpringBootTest
//class BookingServiceImplIntegrationTest {
//    @Autowired
//    BookingService bookingService;
//    //@Autowired
////@InjectMocks
////BookingServiceImpl bookingService;
//    @MockBean
//    ItemRepository itemRepository;
//    //@Autowired
////    @MockBean
////    ItemRequestRepository itemRequestRepository;
//    //@Autowired
//    @MockBean
//    UserRepository userRepository;
//    //@Autowired
//    @MockBean
//    BookingRepository bookingRepository;
//    private final EasyRandom easyRandom = new EasyRandom();
//    private IncomingBookingDto incomingBookingDtoOne;
//    private IncomingBookingDto incomingBookingDtoTwo;
//    private BookingResponseDto bookingResponseDto;
//    private Booking booking;
//    private Item item;
//    private User user;
//
//    @BeforeEach
//    void setUp() {
//        bookingService = new BookingServiceImpl(itemRepository, userRepository, bookingRepository);
//
//        incomingBookingDtoOne = new IncomingBookingDto();
//        incomingBookingDtoTwo = new IncomingBookingDto();
//
//        incomingBookingDtoOne.setBookerId(1L);
//        incomingBookingDtoTwo.setBookerId(2L);
//        incomingBookingDtoOne.setItemId(1L);
//        incomingBookingDtoTwo.setItemId(1L);
//
//        incomingBookingDtoOne.setStart(LocalDateTime.now().plus(1, ChronoUnit.DAYS));
//        incomingBookingDtoOne.setEnd(LocalDateTime.now().plus(2, ChronoUnit.DAYS));
//        incomingBookingDtoTwo.setStart(LocalDateTime.now().plus(3, ChronoUnit.DAYS));
//        incomingBookingDtoTwo.setEnd(LocalDateTime.now().plus(4, ChronoUnit.DAYS));
//
//        IncomingUserDto userDtoOne = easyRandom.nextObject(IncomingUserDto.class);
//        userDtoOne.setEmail("email@mail.ru");
//        // user = userRepository.save(UserMapper.mapToUser(userDtoOne));
//        user = UserMapper.mapToUser(userDtoOne);
//        user.setId(1L);
//        IncomingUserDto userDtoTwo = easyRandom.nextObject(IncomingUserDto.class);
//        userDtoTwo.setEmail("mail@yandex.ru");
//
//
//        // User userTwo = userRepository.save(UserMapper.mapToUser(userDtoTwo));
//        //   userTwo.setId(2L);
//        IncomingItemDto itemDto = easyRandom.nextObject(IncomingItemDto.class);
//        itemDto.setOwnerId(1L);
//        itemDto.setRequestId(1L);
//
//        IncomingItemRequestDto incomingItemRequestDto = new IncomingItemRequestDto();
//        incomingItemRequestDto.setDescription("Description");
//
//        // ItemRequest itemRequest = itemRequestRepository.save(ItemRequestMapper.mapToItemRequest(incomingItemRequestDto, userOne));
//
//        item = ItemMapper.mapToItem(itemDto, user);
//        item.setId(1L);
//        booking = BookingMapper.mapToBooking(incomingBookingDtoOne, item, user);
//        booking.setId(1L);
//        bookingResponseDto = BookingMapper.mapToBookingResponseDto(booking, item);
//        bookingResponseDto.setId(1L);
//    }
//
//    @Test
//        // @DirtiesContext
//    void getAllTest() {
////        BookingResponseDto bookingResponseDtoOne = bookingService.addBooking(incomingBookingDtoOne);
////        BookingResponseDto bookingResponseDtoTwo = bookingService.addBooking(incomingBookingDtoTwo);
////        List<Booking> bookings = bookingRepository.findAll();
////        assertNotNull(bookings);
////        assertEquals(2, bookings.size());
//    }
//
//    @Test
//        //@DirtiesContext
//    void getBookingByIdTest() {
////        BookingResponseDto bookingResponseDtoOne = bookingService.addBooking(incomingBookingDtoOne);
////        BookingResponseDto bookingResponseDtoTwo = bookingService.addBooking(incomingBookingDtoTwo);
////        List<Booking> bookings = bookingRepository.findAll();
////        assertNotNull(bookings);
////        assertEquals(2, bookings.size());
//    }
//
//    @Test
//    @DirtiesContext
//    void getBookingsByOwnerTest() {
////        BookingResponseDto bookingResponseDtoOne = bookingService.addBooking(incomingBookingDtoOne);
////        BookingResponseDto bookingResponseDtoTwo = bookingService.addBooking(incomingBookingDtoTwo);
//    }
//
//    @ParameterizedTest
//    @MethodSource("ru.practicum.shareit.util.TestData#argsProviderFactoryBookingsByBooker")
//    void getBookingsByBookerTest(Long bookerId, StateRequest state) {
//        List<Booking> expectedListBookings;
//        List<BookingResponseDto> result;
//
//        int from = 0;
//        int size = 10;
//        Pageable pageable = PageRequest.of(from / size, size, Sort.by("id").descending());
//        Page<Booking> bookingPage;
//        if (bookerId.equals(99L)) {
//            when(userRepository.findById(bookerId))
//                    .thenThrow(new NotFoundException(String.format("Пользователь с id %d не найден", bookerId)));
//            assertThrows(NotFoundException.class, () -> bookingService.getBookingsByBooker(bookerId, state, from, size));
//        } else {
//            List<BookingResponseDto> expected;
//            switch (state) {
//                case UNSUPPORTED_STATUS:
//                    when(userRepository.findById(bookerId)).thenReturn(Optional.of(user));
//
//                    assertThrows(UnsupportedStatusException.class, () -> bookingService.getBookingsByBooker(bookerId, state, from, size));
//                    break;
//                case ALL:
//                    expectedListBookings = List.of(booking);
//                    bookingPage = new PageImpl<>(expectedListBookings);
//                    when(userRepository.findById(bookerId)).thenReturn(Optional.of(user));
//                    when(bookingRepository.findAllByBooker_Id(bookerId, pageable)).thenReturn(bookingPage);
//                    expected = bookingPage.getContent()
//                            .stream()
//                            .map(bookingResponse -> BookingMapper.mapToBookingResponseDto(bookingResponse, item))
//                            .collect(Collectors.toList());
//                    result = bookingService.getBookingsByBooker(bookerId, state, from, size);
//
//                    assertEquals(expected, result);
//                    break;
//                case CURRENT:
//                case FUTURE:
//                case PAST:
//                case WAITING:
//                case REJECTED:
//                default:
//                    expectedListBookings = Collections.emptyList();
//                    bookingPage = new PageImpl<>(expectedListBookings);
//                    when(userRepository.findById(bookerId)).thenReturn(Optional.of(user));
//                    when(bookingRepository.findAllByBooker_Id(bookerId, pageable)).thenReturn(bookingPage);
//                    expected = bookingPage.getContent()
//                            .stream()
//                            .map(bookingResponse -> BookingMapper.mapToBookingResponseDto(bookingResponse, item))
//                            .collect(Collectors.toList());
//                    result = bookingService.getBookingsByBooker(bookerId, state, from, size);
//
//                    assertEquals(expected, result);
//                    break;
//            }
//        }
//    }
//    /*
//     @Transactional(readOnly = true)
//    @Override
//    public List<BookingResponseDto> getBookingsByBooker(Long bookerId, StateRequest state, Integer from, Integer size) {
//        LocalDateTime dateTimeNow = LocalDateTime.now();
//        User booker = userRepository.findById(bookerId)
//                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с id %d не найден", bookerId)));
//        if (state == StateRequest.UNSUPPORTED_STATUS) {
//            throw new UnsupportedStatusException("Unknown state: UNSUPPORTED_STATUS");
//        }
//        List<Booking> bookings;
//        switch (state) {
//            case ALL:
//                Pageable pageable = PageRequest.of(from / size, size, Sort.by("id").descending());
//                Page<Booking> bookingPage = bookingRepository.findAllByBooker_Id(bookerId, pageable);
//
//                bookings = bookingPage.getContent();
//                break;
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
//                bookings = bookingRepository.findAllByBooker_IdAndStatus(bookerId, Status.REJECTED, SORT_BY_DESC);
//                break;
//            default:
//                bookings = Collections.emptyList();
//        }
//        return bookings
//                .stream()
//                .map(booking -> BookingMapper.mapToBookingResponseDto(booking, booking.getItem()))
//                .collect(Collectors.toList());
//    }
//     */
//
//    @Test
//        // @DirtiesContext
//    void addBookingTest() {
//    }
//
//    @Test
//    @DirtiesContext
//    void isOverlapTimeTest() {
//    }
//
//    @Test
////    @DirtiesContext
//    void updateBookingTest() {
//    }
//
//    @Test
//        //  @DirtiesContext
//    void approvingBookingTest() {
//    }
//}
///*
//package ru.practicum.shareit.booking.service;
//
//import org.jeasy.random.EasyRandom;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.MethodSource;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.data.domain.*;
//import org.springframework.test.annotation.DirtiesContext;
//import ru.practicum.shareit.booking.dto.BookingMapper;
//import ru.practicum.shareit.booking.dto.BookingResponseDto;
//import ru.practicum.shareit.booking.dto.IncomingBookingDto;
//import ru.practicum.shareit.booking.model.Booking;
//import ru.practicum.shareit.booking.model.StateRequest;
//import ru.practicum.shareit.booking.repository.BookingRepository;
//import ru.practicum.shareit.exception.NotFoundException;
//import ru.practicum.shareit.exception.UnsupportedStatusException;
//import ru.practicum.shareit.item.dto.IncomingItemDto;
//import ru.practicum.shareit.item.dto.ItemMapper;
//import ru.practicum.shareit.item.model.Item;
//import ru.practicum.shareit.item.repository.ItemRepository;
//import ru.practicum.shareit.request.dto.IncomingItemRequestDto;
//import ru.practicum.shareit.request.repository.ItemRequestRepository;
//import ru.practicum.shareit.user.dto.IncomingUserDto;
//import ru.practicum.shareit.user.dto.UserMapper;
//import ru.practicum.shareit.user.model.User;
//import ru.practicum.shareit.user.repository.UserRepository;
//
//import java.time.LocalDateTime;
//import java.time.temporal.ChronoUnit;
//import java.util.Collections;
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.when;
//
//@SpringBootTest
//class BookingServiceImplIntegrationTest {
//    @Autowired
//    BookingService bookingService;
//    //@Autowired
////@InjectMocks
////BookingServiceImpl bookingService;
//    @MockBean
//    ItemRepository itemRepository;
//    //@Autowired
////    @MockBean
////    ItemRequestRepository itemRequestRepository;
//    //@Autowired
//    @MockBean
//    UserRepository userRepository;
//    //@Autowired
//    @MockBean
//    BookingRepository bookingRepository;
//    private final EasyRandom easyRandom = new EasyRandom();
//    private IncomingBookingDto incomingBookingDtoOne;
//    private IncomingBookingDto incomingBookingDtoTwo;
//    private BookingResponseDto bookingResponseDto;
//    private Booking booking;
//    private Item item;
//    private User user;
//
//    @BeforeEach
//    void setUp() {
//        bookingService = new BookingServiceImpl(itemRepository, userRepository, bookingRepository);
//
//        incomingBookingDtoOne = new IncomingBookingDto();
//        incomingBookingDtoTwo = new IncomingBookingDto();
//
//        incomingBookingDtoOne.setBookerId(1L);
//        incomingBookingDtoTwo.setBookerId(2L);
//        incomingBookingDtoOne.setItemId(1L);
//        incomingBookingDtoTwo.setItemId(1L);
//
//        incomingBookingDtoOne.setStart(LocalDateTime.now().plus(1, ChronoUnit.DAYS));
//        incomingBookingDtoOne.setEnd(LocalDateTime.now().plus(2, ChronoUnit.DAYS));
//        incomingBookingDtoTwo.setStart(LocalDateTime.now().plus(3, ChronoUnit.DAYS));
//        incomingBookingDtoTwo.setEnd(LocalDateTime.now().plus(4, ChronoUnit.DAYS));
//
//        IncomingUserDto userDtoOne = easyRandom.nextObject(IncomingUserDto.class);
//        userDtoOne.setEmail("email@mail.ru");
//        // user = userRepository.save(UserMapper.mapToUser(userDtoOne));
//        user = UserMapper.mapToUser(userDtoOne);
//        user.setId(1L);
//        IncomingUserDto userDtoTwo = easyRandom.nextObject(IncomingUserDto.class);
//        userDtoTwo.setEmail("mail@yandex.ru");
//
//
//        // User userTwo = userRepository.save(UserMapper.mapToUser(userDtoTwo));
//        //   userTwo.setId(2L);
//        IncomingItemDto itemDto = easyRandom.nextObject(IncomingItemDto.class);
//        itemDto.setOwnerId(1L);
//        itemDto.setRequestId(1L);
//
//        IncomingItemRequestDto incomingItemRequestDto = new IncomingItemRequestDto();
//        incomingItemRequestDto.setDescription("Description");
//
//        // ItemRequest itemRequest = itemRequestRepository.save(ItemRequestMapper.mapToItemRequest(incomingItemRequestDto, userOne));
//
//        item = ItemMapper.mapToItem(itemDto, user);
//        item.setId(1L);
//        booking = BookingMapper.mapToBooking(incomingBookingDtoOne, item, user);
//        booking.setId(1L);
//        bookingResponseDto = BookingMapper.mapToBookingResponseDto(booking, item);
//        bookingResponseDto.setId(1L);
//    }
//
//    @Test
//   // @DirtiesContext
//    void getAllTest() {
////        BookingResponseDto bookingResponseDtoOne = bookingService.addBooking(incomingBookingDtoOne);
////        BookingResponseDto bookingResponseDtoTwo = bookingService.addBooking(incomingBookingDtoTwo);
////        List<Booking> bookings = bookingRepository.findAll();
////        assertNotNull(bookings);
////        assertEquals(2, bookings.size());
//    }
//
//    @Test
//    //@DirtiesContext
//    void getBookingByIdTest() {
////        BookingResponseDto bookingResponseDtoOne = bookingService.addBooking(incomingBookingDtoOne);
////        BookingResponseDto bookingResponseDtoTwo = bookingService.addBooking(incomingBookingDtoTwo);
////        List<Booking> bookings = bookingRepository.findAll();
////        assertNotNull(bookings);
////        assertEquals(2, bookings.size());
//    }
//
//    @Test
//    @DirtiesContext
//    void getBookingsByOwnerTest() {
////        BookingResponseDto bookingResponseDtoOne = bookingService.addBooking(incomingBookingDtoOne);
////        BookingResponseDto bookingResponseDtoTwo = bookingService.addBooking(incomingBookingDtoTwo);
//    }
//
//    @ParameterizedTest
//    @MethodSource("ru.practicum.shareit.util.TestData#argsProviderFactoryBookingsByBooker")
//    void getBookingsByBookerTest(Long bookerId, StateRequest state) {
//        List<Booking> expectedListBookings;
//        List<BookingResponseDto> result;
//
//        int from = 0;
//        int size = 10;
//        Pageable pageable = PageRequest.of(from / size, size, Sort.by("id").descending());
//        Page<Booking> bookingPage;
//        if (bookerId.equals(99L)) {
//            when(userRepository.findById(bookerId))
//                    .thenThrow(new NotFoundException(String.format("Пользователь с id %d не найден", bookerId)));
//            assertThrows(NotFoundException.class, () -> bookingService.getBookingsByBooker(bookerId, state, from, size));
//        } else {
//            List<BookingResponseDto> expected;
//            switch (state) {
//                case UNSUPPORTED_STATUS:
//                    when(userRepository.findById(bookerId)).thenReturn(Optional.of(user));
//
//                    assertThrows(UnsupportedStatusException.class, () -> bookingService.getBookingsByBooker(bookerId, state, from, size));
//                    break;
//                case ALL:
//                    expectedListBookings = List.of(booking);
//                    bookingPage = new PageImpl<>(expectedListBookings);
//                    when(userRepository.findById(bookerId)).thenReturn(Optional.of(user));
//                    when(bookingRepository.findAllByBooker_Id(bookerId, pageable)).thenReturn(bookingPage);
//                    expected = bookingPage.getContent()
//                            .stream()
//                            .map(bookingResponse -> BookingMapper.mapToBookingResponseDto(bookingResponse, item))
//                            .collect(Collectors.toList());
//                    result = bookingService.getBookingsByBooker(bookerId, state, from, size);
//
//                    assertEquals(expected, result);
//                    break;
//                case CURRENT:
//                case FUTURE:
//                case PAST:
//                case WAITING:
//                case REJECTED:
//                default:
//                    expectedListBookings = Collections.emptyList();
//                    bookingPage = new PageImpl<>(expectedListBookings);
//                    when(userRepository.findById(bookerId)).thenReturn(Optional.of(user));
//                    when(bookingRepository.findAllByBooker_Id(bookerId, pageable)).thenReturn(bookingPage);
//                    expected = bookingPage.getContent()
//                            .stream()
//                            .map(bookingResponse -> BookingMapper.mapToBookingResponseDto(bookingResponse, item))
//                            .collect(Collectors.toList());
//                    result = bookingService.getBookingsByBooker(bookerId, state, from, size);
//
//                    assertEquals(expected, result);
//                    break;
//            }
//        }
//    }
//    /*
//     @Transactional(readOnly = true)
//    @Override
//    public List<BookingResponseDto> getBookingsByBooker(Long bookerId, StateRequest state, Integer from, Integer size) {
//        LocalDateTime dateTimeNow = LocalDateTime.now();
//        User booker = userRepository.findById(bookerId)
//                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с id %d не найден", bookerId)));
//        if (state == StateRequest.UNSUPPORTED_STATUS) {
//            throw new UnsupportedStatusException("Unknown state: UNSUPPORTED_STATUS");
//        }
//        List<Booking> bookings;
//        switch (state) {
//            case ALL:
//                Pageable pageable = PageRequest.of(from / size, size, Sort.by("id").descending());
//                Page<Booking> bookingPage = bookingRepository.findAllByBooker_Id(bookerId, pageable);
//
//                bookings = bookingPage.getContent();
//                break;
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
//                bookings = bookingRepository.findAllByBooker_IdAndStatus(bookerId, Status.REJECTED, SORT_BY_DESC);
//                break;
//            default:
//                bookings = Collections.emptyList();
//        }
//        return bookings
//                .stream()
//                .map(booking -> BookingMapper.mapToBookingResponseDto(booking, booking.getItem()))
//                .collect(Collectors.toList());
//    }
//     */
//
//    @Test
//   // @DirtiesContext
//    void addBookingTest() {
//    }
//
//    @Test
//    @DirtiesContext
//    void isOverlapTimeTest() {
//    }
//
//    @Test
////    @DirtiesContext
//    void updateBookingTest() {
//    }
//
//    @Test
//  //  @DirtiesContext
//    void approvingBookingTest() {
//    }
//}*/
