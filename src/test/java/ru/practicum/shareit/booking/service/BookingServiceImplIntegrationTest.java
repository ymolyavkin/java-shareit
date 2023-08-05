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
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.BadRequestException;
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
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;

@SpringBootTest
class BookingServiceImplIntegrationTest {
    @Autowired
    BookingService bookingService;
    @Mock
    ItemRepository itemRepository;
    @Mock
    ItemRequestRepository itemRequestRepository;
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

    @Test
    @DirtiesContext
    void getAllTest() {
        List<Booking> expectedListBookings = Collections.emptyList();
        when(bookingRepository.findAll())
                .thenReturn(expectedListBookings);
        List<Booking> actualListBooking = bookingService.getAll();

        assertEquals(expectedListBookings, actualListBooking);
    }

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

            assertThrows(NotFoundException.class, () -> bookingService.getBookingById(bookingId, userId));//
        }
        if (userId != 1 && userId != 3) {
            Mockito.when(bookingRepository.getReferenceById(bookingId)).thenReturn(booking);
            Mockito.when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
            Throwable thrown = assertThrows(NotFoundException.class, () -> {
                bookingService.getBookingById(bookingId, userId);
            });
            assertNotNull(thrown.getMessage());
        } else {
            Mockito.when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
            Mockito.when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

            Mockito.when(bookingRepository.getReferenceById(booking.getId())).thenReturn(booking);
            BookingResponseDto actual = bookingService.getBookingById(bookingId, userId);
            BookingResponseDto expected = BookingMapper.mapToBookingResponseDto(booking, item);
            assertEquals(expected, actual);
        }
    }

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
        } else if (state == StateRequest.UNSUPPORTED_STATUS) {
            Mockito.when(userRepository.findById(ownerId)).thenReturn(Optional.of(booker));
            assertThrows(UnsupportedStatusException.class, () -> bookingService.getBookingsByOwner(ownerId, state, from, size));
        } else {
            List<BookingResponseDto> expected;
            List<Long> itemIdsByOwner = List.of(ownerId);
            expectedListBookings = Collections.emptyList();

            bookingPage = new PageImpl<>(expectedListBookings);

            List<Booking> bookingList = bookingPage.getContent();
            Mockito.when(userRepository.findById(ownerId)).thenReturn(Optional.of(booker));
            Mockito.when(bookingRepository.findByItem_IdInOrderByStartDesc(itemIdsByOwner, pageable)).thenReturn(bookingPage);
            switch (state) {
                case ALL:
                    expected = bookingList
                            .stream()
                            .map(bookingResponse -> BookingMapper.mapToBookingResponseDto(bookingResponse, item))
                            .collect(Collectors.toList());

                    when(bookingRepository.findByItem_IdInOrderByStartDesc(anyList(), any())).thenReturn(bookingPage);
                    result = bookingService.getBookingsByOwner(ownerId, state, from, size);

                    assertEquals(expected, result);
                case CURRENT:
                    expected = bookingList
                            .stream()
                            .map(bookingResponse -> BookingMapper.mapToBookingResponseDto(bookingResponse, item))
                            .collect(Collectors.toList());

                    when(bookingRepository.findByItem_IdInOrderByStartDesc(anyList(), any())).thenReturn(bookingPage);
                    result = bookingService.getBookingsByOwner(ownerId, state, from, size);

                    assertEquals(expected, result);
                case FUTURE:
                    expected = bookingList
                            .stream()
                            .map(bookingResponse -> BookingMapper.mapToBookingResponseDto(bookingResponse, item))
                            .collect(Collectors.toList());

                    when(bookingRepository.findByItem_IdInOrderByStartDesc(anyList(), any())).thenReturn(bookingPage);
                    result = bookingService.getBookingsByOwner(ownerId, state, from, size);

                    assertEquals(expected, result);
                case PAST:
                    expected = bookingList
                            .stream()
                            .map(bookingResponse -> BookingMapper.mapToBookingResponseDto(bookingResponse, item))
                            .collect(Collectors.toList());

                    when(bookingRepository.findByItem_IdInOrderByStartDesc(anyList(), any())).thenReturn(bookingPage);
                    result = bookingService.getBookingsByOwner(ownerId, state, from, size);

                    assertEquals(expected, result);
                case WAITING:
                    expected = bookingList
                            .stream()
                            .map(bookingResponse -> BookingMapper.mapToBookingResponseDto(bookingResponse, item))
                            .collect(Collectors.toList());

                    when(bookingRepository.findByItem_IdInOrderByStartDesc(anyList(), any())).thenReturn(bookingPage);
                    result = bookingService.getBookingsByOwner(ownerId, state, from, size);

                    assertEquals(expected, result);
                case REJECTED:
                    expected = bookingList
                            .stream()
                            .map(bookingResponse -> BookingMapper.mapToBookingResponseDto(bookingResponse, item))
                            .collect(Collectors.toList());

                    when(bookingRepository.findByItem_IdInOrderByStartDesc(anyList(), any())).thenReturn(bookingPage);
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
        switch (bookerId) {
            case 1:
                Mockito.when(userRepository.findById((long) bookerId)).thenReturn(Optional.of(booker));
                Mockito.when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
                Mockito.when(bookingRepository.save(Mockito.any())).thenReturn(booking);
                BookingResponseDto actualBooking = bookingService.addBooking(incomingBookingDtoOne);
                break;
            case -1:
                Mockito.when(userRepository.findById((long) bookerId)).thenReturn(Optional.of(booker));
                doThrow(NotFoundException.class).when(userRepository).findById((long) bookerId);
                break;
            default:
                Mockito.when(userRepository.findById((long) bookerId)).thenReturn(Optional.empty());
                doThrow(NotFoundException.class).when(userRepository).findById((long) bookerId);

                assertThrows(NotFoundException.class, () -> bookingService.addBooking(incomingBookingDtoOne));
        }

    }

    @Test
    @DirtiesContext
    void updateBooking_whenUnknownUser_thenThrown() {
        when(bookingRepository.getReferenceById(anyLong()))
                .thenThrow(new NotFoundException("Бронирование не найдено"));

        assertThrows(NotFoundException.class, () -> bookingService.updateBooking(incomingBookingDtoOne, 99L, 1L));
        verify(bookingRepository, never()).saveAndFlush(Mockito.any(Booking.class));
    }

    @Test
    @DirtiesContext
    void updateBooking_whenUnknownItem_thenThrown() {
        Mockito.lenient().when(bookingRepository.getReferenceById(anyLong()))
                .thenReturn(booking);
        when(itemRepository.findById(anyLong()))
                .thenThrow(new NotFoundException(String.format("Вещь с id %d не найдена", booking.getItemId())));

        assertThrows(NotFoundException.class, () -> bookingService.updateBooking(incomingBookingDtoOne, 99L, 1L));
        verify(bookingRepository, never()).saveAndFlush(Mockito.any(Booking.class));
    }

    @Test
    @DirtiesContext
    void updateBooking_whenCorrectAll_thenUpdate() {
        Mockito.lenient().when(bookingRepository.getReferenceById(anyLong()))
                .thenReturn(booking);
        Mockito.lenient().when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(item));

        when(bookingRepository.saveAndFlush(booking)).thenReturn(booking);
        BookingResponseDto expected = BookingMapper.mapToBookingResponseDto(booking, item);
        BookingResponseDto actual = bookingService.updateBooking(incomingBookingDtoOne, booking.getId(), 1L);

        assertEquals(expected, actual);
        verify(bookingRepository, times(1)).saveAndFlush(Mockito.any(Booking.class));
    }

    @Test
    @DirtiesContext
    void approvingBooking_whenUnknownBooking_thenThrown() {
        when(bookingRepository.findById(anyLong()))
                .thenThrow(new NotFoundException("Бронирование не найдено"));

        assertThrows(NotFoundException.class, () -> bookingService.approvingBooking(99L, 99L, true));
        verify(bookingRepository, never()).saveAndFlush(Mockito.any(Booking.class));
    }

    @Test
    @DirtiesContext
    void approvingBooking_whenUnknownItem_thenThrown() {
        Mockito.lenient().when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
        when(itemRepository.findById(anyLong()))
                .thenThrow(new NotFoundException(String.format("Вещь с id %d не найдена", booking.getItemId())));

        assertThrows(NotFoundException.class, () -> bookingService.approvingBooking(99L, 99L, true));
        verify(bookingRepository, never()).saveAndFlush(Mockito.any(Booking.class));
    }

    @Test
    @DirtiesContext
    void approvingBooking_whenNotOwner_thenThrown() {
        Mockito.lenient().when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
        Mockito.lenient().when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));

        Throwable thrown = assertThrows(NotFoundException.class, () -> {
            bookingService.approvingBooking(1L, 2L, true);
        });
        assertNotNull(thrown.getMessage());

        verify(bookingRepository, never()).saveAndFlush(Mockito.any(Booking.class));
    }

    @Test
    void getApproveStatusApprovedTest() {
        Long userId = owner.getId();
        Long bookingId = booking.getId();
        BookingResponseDto bookingDto = BookingMapper.mapToBookingResponseDto(booking, item);
        BookingResponseDto bookingDtoActual;
        when(bookingRepository.findById(any())).thenReturn(Optional.ofNullable(booking));
        when(itemRepository.findById(any())).thenReturn(Optional.ofNullable(item));
        bookingDtoActual = bookingService.approvingBooking(userId, bookingId, true);
        bookingDto.setStatus(Status.APPROVED);

        assertEquals(bookingDto.getStatus(), bookingDtoActual.getStatus());
    }

    @Test
    void getApproveStatusRejectedTest() {
        Long userId = owner.getId();
        Long bookingId = booking.getId();
        BookingResponseDto bookingDto = BookingMapper.mapToBookingResponseDto(booking, item);
        BookingResponseDto bookingDtoActual;
        when(bookingRepository.findById(any())).thenReturn(Optional.ofNullable(booking));
        when(itemRepository.findById(any())).thenReturn(Optional.ofNullable(item));
        bookingDtoActual = bookingService.approvingBooking(userId, bookingId, false);
        bookingDto.setStatus(Status.REJECTED);

        assertEquals(bookingDto.getStatus(), bookingDtoActual.getStatus());
    }

    @Test
    void getApproveWithWrongUserIdValidationTest() {
        Long userId = 99L;
        Long bookingId = booking.getId();
        when(bookingRepository.findById(any())).thenReturn(Optional.ofNullable(booking));

        assertThrows(NotFoundException.class, () -> bookingService.approvingBooking(userId, bookingId, true));
    }

    @Test
    void getApproveWithNotAvailableExceptionValidationTest() {
        Long userId = owner.getId();
        Long bookingId = booking.getId();
        Mockito.lenient().when(itemRepository.findById(any())).thenReturn(Optional.ofNullable(item));
        when(bookingRepository.findById(any())).thenReturn(Optional.ofNullable(booking));
        booking.setStatus(Status.APPROVED);
        assertThrows(BadRequestException.class, () -> bookingService.approvingBooking(userId, bookingId, true));
    }
}