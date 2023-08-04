package ru.practicum.shareit.item.service;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NoneXSharerUserIdException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.OwnerMismatchException;
import ru.practicum.shareit.item.comment.CommentRepository;
import ru.practicum.shareit.item.dto.IncomingItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemLastNextDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.IncomingItemRequestDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

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
    private final int from = 0;
    private final int size = 10;
    private Long requesterId = 1L;
    private Long ownerId;

    @BeforeEach
    public void setUp() {
        // itemService = new ItemServiceImpl(itemRepository, userRepository, bookingRepository, commentRepository);
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
        incomingItemDto.setOwnerId(ownerId);
        item = ItemMapper.mapToItem(incomingItemDto, userOwner);
        item.setId(1L);
    }

    @Test
    void getItemById() {
        when(itemRepository.getReferenceById(Mockito.anyLong()))
                .thenReturn(itemOne);
        ItemLastNextDto itemResponse = itemService.getItemById(1L, 1L);
        assertEquals(1L, itemResponse.getId());
    }

    @Test
    void getItemsLastNextBookingByUser() {

    }

    @Test
    void getItemsByUser() {
    }

    @Test
    void addItem() {
        incomingItemDto.setOwnerId(-1L);
        Throwable thrown = assertThrows(NoneXSharerUserIdException.class, () -> {
            itemService.addItem(incomingItemDto);
        });
        assertNotNull(thrown.getMessage());
    }

    @Test
    void updateItem_whenWrongUser_thenThrown() {
        when(itemRepository.findById(99L))
                .thenThrow(new NotFoundException("Вещь не найдена"));

        assertThrows(NotFoundException.class, () -> itemService.updateItem(incomingItemDto, 99L, 1L));
        verify(itemRepository, never()).saveAndFlush(Mockito.any(Item.class));
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
                .findByNameIsContainingIgnoreCaseOrDescriptionIsContainingIgnoreCase("searchText", "searchText"))
                .thenReturn(List.of(item));
        List<ItemDto> exppectedItemDtoList = List.of(ItemMapper.mapToItemDto(item));
        List<ItemDto> actualItemDtoList = itemService.searchItemsByText("searchText", from, size);

        assertEquals(exppectedItemDtoList.get(0).getId(), actualItemDtoList.get(0).getId());
        assertEquals(exppectedItemDtoList.get(0).getDescription(), actualItemDtoList.get(0).getDescription());
        assertEquals(exppectedItemDtoList.get(0).getName(), actualItemDtoList.get(0).getName());
    }

    @Test
    void addComment() {
    }
}