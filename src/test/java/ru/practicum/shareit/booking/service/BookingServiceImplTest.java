package ru.practicum.shareit.booking.service;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.dto.IncomingBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NoneXSharerUserIdException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.IncomingItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.IncomingItemRequestDto;
import ru.practicum.shareit.user.dto.IncomingUserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

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
    private IncomingBookingDto incomingBookingDtoOne;
    private IncomingBookingDto incomingBookingDtoTwo;
    private User booker;
    private User otherBooker;
    private User owner;
    private Item item;
    private Booking booking;
    private Booking otherBooking;
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
        //   bookingService = new BookingServiceImpl(itemRepository, userRepository, bookingRepository);

        incomingBookingDtoOne = new IncomingBookingDto();
        incomingBookingDtoTwo = new IncomingBookingDto();

        incomingBookingDtoOne.setBookerId(bookerId);
        incomingBookingDtoTwo.setBookerId(ownerId);
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
        otherBooking = BookingMapper.mapToBooking(incomingBookingDtoTwo, item, booker);
        booking.setId(bookingId);
        bookingResponseDto = BookingMapper.mapToBookingResponseDto(booking, item);
    }

    @Test
    void getBookingByIdWithWrongBookingIdTest() {
        Long bookingId = 99L;
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> bookingService.getBookingById(booker.getId(), bookingId));
    }

    @Test
    void getBookingByIdWithWrongUserIdTest() {
        Long bookingId = 1L;
        Long userId = 99L;
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));

        assertThrows(NotFoundException.class, () -> bookingService.getBookingById(userId, bookingId));
    }

    @Test
    public void addBookingTest() {
        long bookerId = 3L;
        long bookingId = booking.getId();
        long otherBookingId = owner.getId();
        incomingBookingDtoOne.setBookerId(bookerId);
        long itemId = incomingBookingDtoOne.getItemId();
        // bookingResponseDto userRepository.findById(bookerId)

        Mockito.lenient().when(userRepository.findById(bookerId)).thenReturn(Optional.of(booker));
        Mockito.lenient().when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        List<Booking> allBookingsByItem = List.of(booking);
        when(userRepository.findById(otherBookingId)).thenReturn(Optional.of(booker));
        when(bookingRepository.findByItem_Id(itemId)).thenReturn(allBookingsByItem);
    //    when(bookingRepository.getReferenceById(anyLong())).thenReturn(booking);
     //   when(bookingRepository.findById(anyLong())).thenReturn(Optional.ofNullable(booking));
     //   when(bookingRepository.getReferenceById(otherBookingId)).thenReturn(otherBooking);
//        when(bookingRepository.save(any()))
//                .thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));
        when(bookingRepository.save(any())).thenReturn(booking);
        BookingResponseDto bookingdto = bookingService.addBooking(incomingBookingDtoTwo);

        assertThat(bookingdto).hasFieldOrProperty("id");
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
 /*
    @Test
    void createBookingsWithFalseAvailableTest() {
        Long userId = userWithBooking.getId();
        Long itemId = item.getId();
        item.setAvailable(false);
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(userRepository.findById(any())).thenReturn(Optional.of(userWithBooking));
        BookingShortDto shortDto = new BookingShortDto(userId, start, end, itemId);
        assertThrows(NotAvailableException.class, () -> bookingService.createBooking(userId, shortDto));

        verify(bookingRepository, never()).save(any());
    }

    @Test
    void createBookingReturnItemNotFoundExceptionTest() {
        Long userId = userWithBooking.getId();
        Long itemId = 9999L;
        when(itemRepository.findById(itemId)).thenReturn(Optional.empty());
        BookingShortDto shortDto = new BookingShortDto(userId, start, end, itemId);

        assertThrows(NotFoundException.class, () -> bookingService.createBooking(userId, shortDto));
    }

    @Test
    void createBookingReturnItemNotAvailableExceptionDateTest() {
        Long userId = userWithBooking.getId();
        Long itemId = item.getId();
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().minusDays(2);

        when(itemRepository.findById(itemId)).thenThrow(NotAvailableException.class);
        BookingShortDto shortDto = new BookingShortDto(userId, start, end, itemId);

        assertThrows(NotAvailableException.class, () -> bookingService.createBooking(userId, shortDto));
    }

    @Test
    void createBookingReturnItemNotAvailableExceptionItemIdTest() {
        Long userId = userWithBooking.getId();
        Long itemId = 999L;

        when(itemRepository.findById(itemId)).thenThrow(NotAvailableException.class);
        BookingShortDto shortDto = new BookingShortDto(userId, start, end, itemId);

        assertThrows(NotAvailableException.class, () -> bookingService.createBooking(userId, shortDto));
    }

     */

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
    void updateBooking_whenUnknownUser_thenThrown() {
        long ownerId = -1L;
        long bookingId = 1L;
        Throwable thrown = assertThrows(NoneXSharerUserIdException.class, () -> {
            bookingService.updateBooking(incomingBookingDtoOne, bookingId, ownerId);
        });
        assertNotNull(thrown.getMessage());
    }

    @Test
    void updateBooking_whenUnknownBooking_thenThrown() {
        long bookingId = 99L;
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> bookingService.getBookingById(booker.getId(), bookingId));
    }

    @Test
    void updateBooking_whenUnknownItem_thenThrown() {
        long bookingId = 1L;
        long itemId = 99L;
        Mockito.lenient().when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> bookingService.getBookingById(booker.getId(), bookingId));
    }

    @Test
    void updateBooking_whenCorrectAll_thenUpdate() {
        long bookingId = 1L;
        long bookerId = 1L;
        Mockito.lenient().when(bookingRepository.getReferenceById(anyLong())).thenReturn(booking);
        Mockito.lenient().when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));

        BookingResponseDto expected = bookingResponseDto;
        when(bookingRepository.getReferenceById(anyLong())).thenReturn(booking);
        BookingResponseDto actual = bookingService.updateBooking(incomingBookingDtoOne, bookingId, bookerId);
        assertEquals(actual, expected);
        verify(bookingRepository, times(1)).saveAndFlush(any());
    }

    @Test
    void approvingBooking_whenNotOwner_thenThrown() {
        long ownerId = 4L;
        long bookingId = 1L;
        Throwable thrown = assertThrows(NotFoundException.class, () -> {
            bookingService.approvingBooking(bookingId, ownerId, true);
        });
        assertNotNull(thrown.getMessage());
    }

    @Test
    void approvingBooking_whenAllCorrect_thenApproved() {
        long bookingId = 1L;
        long ownerId = 1L;
        Mockito.lenient().when(bookingRepository.findById(anyLong())).thenReturn(Optional.ofNullable(booking));
        Mockito.lenient().when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        bookingResponseDto.setStatus(Status.APPROVED);
        BookingResponseDto expected = bookingResponseDto;

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.ofNullable(booking));

        BookingResponseDto actual = bookingService.approvingBooking(bookingId, ownerId, true);

        assertEquals(actual, expected);
        verify(bookingRepository, times(1)).saveAndFlush(any());
    }

    @Test
    void approvingBooking_whenAllCorrect_thenRejected() {
        long bookingId = 1L;
        long ownerId = 1L;
        Mockito.lenient().when(bookingRepository.findById(anyLong())).thenReturn(Optional.ofNullable(booking));
        Mockito.lenient().when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        bookingResponseDto.setStatus(Status.REJECTED);
        BookingResponseDto expected = bookingResponseDto;

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.ofNullable(booking));

        BookingResponseDto actual = bookingService.approvingBooking(bookingId, ownerId, false);

        assertEquals(actual, expected);
        verify(bookingRepository, times(1)).saveAndFlush(any());
    }

    @Test
    void approvingBooking_whenApprovedIsNull_thenNotApproved() {
        long bookingId = 1L;
        long ownerId = 1L;
        Mockito.lenient().when(bookingRepository.findById(anyLong())).thenReturn(Optional.ofNullable(booking));
        Mockito.lenient().when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.ofNullable(booking));

        bookingService.approvingBooking(bookingId, ownerId, null);

        verify(bookingRepository, never()).saveAndFlush(any());
    }
}