package ru.practicum.shareit.item.service;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareitserver.booking.dto.BookingLastNextDto;
import ru.practicum.shareitserver.booking.dto.BookingMapper;
import ru.practicum.shareitserver.booking.model.Booking;
import ru.practicum.shareitserver.booking.model.Status;
import ru.practicum.shareitserver.booking.repository.BookingRepository;
import ru.practicum.shareitserver.exception.CommentErrorException;
import ru.practicum.shareitserver.exception.NotFoundException;
import ru.practicum.shareitserver.exception.OwnerMismatchException;
import ru.practicum.shareitserver.item.comment.Comment;
import ru.practicum.shareitserver.item.comment.CommentRepository;
import ru.practicum.shareitserver.item.comment.dto.CommentDto;
import ru.practicum.shareitserver.item.comment.dto.CommentMapper;
import ru.practicum.shareitserver.item.comment.dto.IncomingCommentDto;
import ru.practicum.shareitserver.item.dto.IncomingItemDto;
import ru.practicum.shareitserver.item.dto.ItemDto;
import ru.practicum.shareitserver.item.dto.ItemLastNextDto;
import ru.practicum.shareitserver.item.dto.ItemMapper;
import ru.practicum.shareitserver.item.model.Item;
import ru.practicum.shareitserver.item.repository.ItemRepository;
import ru.practicum.shareitserver.item.service.ItemServiceImpl;
import ru.practicum.shareitserver.request.dto.IncomingItemRequestDto;
import ru.practicum.shareitserver.user.model.User;
import ru.practicum.shareitserver.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {
    private final EasyRandom easyRandom = new EasyRandom();
    @InjectMocks
    private ItemServiceImpl itemService;
    @Mock
    ItemRepository itemRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    BookingRepository bookingRepository;
    @Mock
    CommentRepository commentRepository;

    private Item item;
    private Item itemOne;
    private Item itemTwo;
    private User userOwner;
    private User userAuthor;
    private IncomingItemDto incomingItemDto;
    private IncomingItemRequestDto incomingItemRequestDto;
    private IncomingCommentDto incomingCommentDto;
    private Comment comment;
    private final int from = 0;
    private final int size = 10;
    private Long requesterId = 1L;
    private Long ownerId;
    private Booking lastBooking;
    private Booking nextBooking;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private final LocalDateTime dateTime = LocalDateTime.parse("2023-01-14 11:04", formatter);

    @BeforeEach
    public void setUp() {
        userOwner = new User();
        userOwner.setEmail("userowner@email.ru");
        userOwner.setName("ownerName");
        userOwner.setId(2L);
        userAuthor = new User();
        userAuthor.setEmail("user@email.ru");
        userAuthor.setName("userName");
        userAuthor.setId(1L);
        itemOne = easyRandom.nextObject(Item.class);
        itemTwo = easyRandom.nextObject(Item.class);
        itemOne.setId(1L);
        itemTwo.setId(2L);
        incomingItemDto = new IncomingItemDto();
        incomingItemDto.setName("ItemName");
        incomingItemDto.setDescription("DescriptionItem");
        incomingItemDto.setAvailable(true);
        incomingItemDto.setOwnerId(userOwner.getId());
        item = ItemMapper.mapToItem(incomingItemDto, userOwner);
        item.setId(1L);
        LocalDateTime dateTimeNow = LocalDateTime.now();
        lastBooking = new Booking(1L, dateTime, dateTime.plusDays(1), item, userAuthor, Status.WAITING);
        nextBooking = new Booking(2L, dateTime.plusDays(2), dateTime.plusDays(3), item, userAuthor, Status.WAITING);
        incomingCommentDto = new IncomingCommentDto();
        incomingCommentDto.setText("incomingCommentDtoText");
        comment = Comment
                .builder()
                .text(incomingCommentDto.getText())
                .build();
        comment.setAuthor(userAuthor);
        comment.setItem(item);
        comment.setCreated(LocalDateTime.now());
    }

    @Test
    void getItemById() {
        when(itemRepository.getReferenceById(Mockito.anyLong()))
                .thenReturn(itemOne);
        ItemLastNextDto itemResponse = itemService.getItemById(1L, 1L);
        assertEquals(1L, itemResponse.getId());
    }

    @Test
    void getItemByIdWithLastNextBooking() {
        Long itemId = 2L;
        Long ownerId = 4L;
        item.setId(itemId);
        userOwner.setId(ownerId);

        Mockito.lenient().when(itemRepository.getReferenceById(Mockito.anyLong()))
                .thenReturn(item);

        List<Booking> bookings = Collections.emptyList();
        when(bookingRepository.findByItem_IdAndStartBeforeAndStatusOrderByStartDesc(anyLong(), any(), any()))
                .thenReturn(bookings);
        ItemLastNextDto itemResponse = itemService.getItemById(itemId, ownerId);
        assertEquals(itemId, itemResponse.getId());
    }

    @Test
    void getItemsLastNextBookingByUser() {
        Pageable pageable = PageRequest.of(from, size);
        List<Item> items = List.of(item);
        Page<Item> itemPage = new PageImpl<>(items);

        when(itemRepository.findAllByOwnerId(anyLong(), any())).thenReturn(itemPage);
        List<Comment> comments = Collections.emptyList();

        List<ItemLastNextDto> expected = List.of(ItemMapper.mapToItemLastNextDto(item, null, null, comments));
        List<ItemLastNextDto> actual = itemService.getItemsLastNextBookingByUser(userOwner.getId(), from, size);

        assertTrue(actual.size() > 0);
        assertEquals(expected, actual);
    }

    @Test
    void addItem_whenNotUser_thenThrown() {
        incomingItemDto.setOwnerId(-1L);
        Throwable thrown = assertThrows(NotFoundException.class, () -> {
            itemService.addItem(incomingItemDto);
        });
        assertNotNull(thrown.getMessage());
    }

    @Test
    void addItem_whenUnknownUser_thenThrown() {
        incomingItemDto.setOwnerId(99L);
        Throwable thrown = assertThrows(NotFoundException.class, () -> {
            itemService.addItem(incomingItemDto);
        });
        assertNotNull(thrown.getMessage());
    }

    @Test
    void addItem() {
        Long userId = incomingItemDto.getOwnerId();

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userOwner));
        userRepository.findById(userId);
        when(itemRepository.save(any())).thenReturn(item);

        ItemDto actualDto = itemService.addItem(incomingItemDto);

        assertEquals(incomingItemDto.getName(), actualDto.getName());
        assertEquals(incomingItemDto.getDescription(), actualDto.getDescription());
        assertEquals(incomingItemDto.getRequestId(), actualDto.getRequestId());
        assertThat(actualDto).hasFieldOrProperty("id");
    }

    @Test
    void updateItem_whenUserNotEqualsOwner_thenThrown() {
        incomingItemDto.setOwnerId(userOwner.getId());
        Long userId = userAuthor.getId();
        Long itemId = itemOne.getId();
        Mockito.lenient().when(itemRepository.findById(itemId)).thenReturn(Optional.of(itemOne));
        Mockito.lenient().when(userRepository.findById(userId)).thenReturn(Optional.of(userAuthor));

        Throwable thrown = assertThrows(OwnerMismatchException.class, () -> {
            itemService.updateItem(incomingItemDto, itemId, userId);
        });
        assertNotNull(thrown.getMessage());
    }

    @Test
    void updateItem_whenUserEqualsOwner_thenUpdate() {
        incomingItemDto.setOwnerId(userOwner.getId());
        Long userId = userOwner.getId();
        Long itemId = item.getId();

        Mockito.lenient().when(userRepository.findById(userId)).thenReturn(Optional.of(userAuthor));
        Mockito.when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

        Mockito.when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        ItemDto actualItem = itemService.updateItem(incomingItemDto, itemId, userId);
        ItemDto expectedItem = ItemMapper.mapToItemDto(item);

        assertEquals(actualItem, expectedItem);

        verify(itemRepository, times(1)).saveAndFlush(any());
    }

    @Test
    void updateItemByIdNewNameTest() {
        Long itemId = item.getId();
        Long userId = userOwner.getId();

        when(userRepository.findById(userId)).thenReturn(Optional.of(userOwner));
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(itemRepository.saveAndFlush(any())).thenReturn(item);

        userRepository.findById(userId);
        incomingItemDto.setName("new_name");
        ItemDto itemDto = itemService.updateItem(incomingItemDto, itemId, userId);
        assertThat(itemDto.getName()).isEqualTo("new_name");
    }

    @Test
    void updateItemByIdNewAvailableTeat() {
        Long itemId = item.getId();
        Long userId = userOwner.getId();

        when(userRepository.findById(userId)).thenReturn(Optional.of(userOwner));
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(itemRepository.saveAndFlush(any())).thenReturn(item);

        userRepository.findById(userId);
        incomingItemDto.setAvailable(false);
        ItemDto itemDto = itemService.updateItem(incomingItemDto, itemId, userId);
        assertFalse(itemDto.isAvailable());
    }

    @Test
    void updateItemByIdNewDescriptionTest() {
        Long itemId = item.getId();
        Long userId = userOwner.getId();

        when(userRepository.findById(userId)).thenReturn(Optional.of(userOwner));
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(itemRepository.saveAndFlush(any())).thenReturn(item);

        userRepository.findById(userId);
        incomingItemDto.setDescription("Newest_description");
        ItemDto itemDto = itemService.updateItem(incomingItemDto, itemId, userId);
        assertThat(itemDto.getDescription().equals("Newest_description"));
    }

    @Test
    void searchItemsByText() {
        when(itemRepository
                .findByNameOrDescriptionAndAvailable("searchText"))
                .thenReturn(List.of(item));
        List<ItemDto> exppectedItemDtoList = List.of(ItemMapper.mapToItemDto(item));
        List<ItemDto> actualItemDtoList = itemService.searchItemsByText("searchText", from, size);

        assertEquals(exppectedItemDtoList.get(0).getId(), actualItemDtoList.get(0).getId());
        assertEquals(exppectedItemDtoList.get(0).getDescription(), actualItemDtoList.get(0).getDescription());
        assertEquals(exppectedItemDtoList.get(0).getName(), actualItemDtoList.get(0).getName());
    }

    @Test
    void addComment() {
        long userId = userAuthor.getId();
        long itemId = item.getId();
        Mockito.lenient().when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(userAuthor));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.ofNullable(item));
        when(bookingRepository.existsByBooker_IdAndEndBeforeAndStatus(anyLong(), any(), any()))
                .thenReturn(true);
        CommentDto expected = CommentMapper.mapToCommentDto(comment);

        when(commentRepository.save(any()))
                .thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));
        CommentDto actual = itemService.addComment(incomingCommentDto, userId, itemId);
        expected.setId(1L);
        actual.setId(1L);
        assertEquals(expected, actual);
        assertEquals(expected.getAuthorName(), actual.getAuthorName());
        assertEquals(expected.getText(), actual.getText());
    }

    @Test
    void addComment_whenCommentError_thenThrown() {
        long userId = userAuthor.getId();
        long itemId = item.getId();
        Mockito.lenient().when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(userAuthor));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.ofNullable(item));
        when(bookingRepository.existsByBooker_IdAndEndBeforeAndStatus(anyLong(), any(), any()))
                .thenReturn(false);
        Throwable thrown = assertThrows(CommentErrorException.class, () -> {
            itemService.addComment(incomingCommentDto, userId, itemId);
        });
        assertNotNull(thrown.getMessage());
    }

    @Test
    void isLastTest() {
        lastBooking.setStart(LocalDateTime.now().minusHours(3));
        boolean result = itemService.isLast(lastBooking);
        assertTrue(result);

        lastBooking.setStart(LocalDateTime.now().plusMinutes(1));
        result = itemService.isLast(lastBooking);
        assertFalse(result);

        lastBooking.setStart(LocalDateTime.now());
        result = itemService.isLast(lastBooking);
        assertTrue(result);

        lastBooking.setStart(LocalDateTime.now().plus(1L, ChronoUnit.MILLIS));
        result = itemService.isLast(lastBooking);
        assertTrue(result);
    }

    @Test
    void aroundTime() {
        LocalDateTime dateTime = LocalDateTime.now();
        Booking booking1 = new Booking(3L, dateTime, dateTime.plusDays(1), item, userAuthor, Status.APPROVED);
        Booking booking2 = new Booking(4L, dateTime.plusDays(2), dateTime.plusDays(5), item, userAuthor, Status.APPROVED);
        Booking booking3 = new Booking(5L, dateTime, dateTime.plusDays(1), item, userAuthor, Status.APPROVED);
        Booking booking4 = new Booking(6L, dateTime, dateTime.plusDays(7), item, userAuthor, Status.APPROVED);
        Booking booking5 = new Booking(7L, dateTime.plusDays(9), dateTime.plusDays(3), item, userAuthor, Status.APPROVED);
        List<Booking> bookings = List.of(booking1, booking2, booking3, booking4, booking5);
        Map<Item, List<Booking>> bookingsMap = new HashMap<>();
        bookingsMap.put(item, bookings);
        Map<String, BookingLastNextDto> actual = itemService.aroundTime(item, bookingsMap);
        Map<String, BookingLastNextDto> expected = new HashMap<>(2);
        expected.put("last", BookingMapper.mapToBookingLastNextDto(booking1));
        expected.put("next", BookingMapper.mapToBookingLastNextDto(booking1));

        assertEquals(expected.keySet(), actual.keySet());
        BookingLastNextDto expectedValue = expected.get("last");
        BookingLastNextDto actualValue = expected.get("last");
        assertEquals(expectedValue, actualValue);

        BookingLastNextDto expectedValue2 = expected.get("next");
        BookingLastNextDto actualValue2 = expected.get("next");
        assertEquals(expectedValue2, actualValue2);
    }
}