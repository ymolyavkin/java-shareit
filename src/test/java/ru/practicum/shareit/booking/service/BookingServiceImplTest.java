package ru.practicum.shareit.booking.service;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.dto.IncomingBookingDto;
import ru.practicum.shareit.booking.model.Booking;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

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


    /*
    public BookingResponseDto updateBooking(IncomingBookingDto incomingBookingDto, Long bookingId, Long bookerId) {
     if (ownerId.equals(-1L)) {
                throw new NoneXSharerUserIdException("Не указан владелец вещи");
            }
            Booking booking = bookingRepository.getReferenceById(bookingId);
            Item item = itemRepository.findById(booking.getItemId())
                    .orElseThrow(() -> new NotFoundException(String.format("Вещь с id %d не найдена", booking.getItemId())));

            booking.setStart(incomingBookingDto.getStart());
            booking.setEnd(incomingBookingDto.getEnd());
            booking.setStatus(incomingBookingDto.getStatus());
            bookingRepository.saveAndFlush(booking);

            return BookingMapper.mapToBookingResponseDto(booking, item);
        }
     */
    @Test
    void approvingBooking() {
    }
}