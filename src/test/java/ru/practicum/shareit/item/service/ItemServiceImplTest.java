package ru.practicum.shareit.item.service;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.comment.CommentRepository;
import ru.practicum.shareit.item.dto.IncomingItemDto;
import ru.practicum.shareit.item.dto.ItemLastNextDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {
    private final EasyRandom easyRandom = new EasyRandom();
    private ItemServiceImpl itemService;
    @Mock
    ItemRepository itemRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    BookingRepository bookingRepository;
    @Mock
    CommentRepository commentRepository;
    /*
        ItemServiceImplTest() {
        }*/
    private Item itemOne;
    private Item itemTwo;

    @BeforeEach
    public void setUp() {
        itemService = new ItemServiceImpl(itemRepository, userRepository, bookingRepository, commentRepository);
        itemOne = easyRandom.nextObject(Item.class);
        itemTwo = easyRandom.nextObject(Item.class);
        itemOne.setId(1L);
        itemTwo.setId(2L);
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
    }

    @Test
    void updateItem() {
        IncomingItemDto incomingItemDto = easyRandom.nextObject(IncomingItemDto.class);
        when(itemRepository.findById(99L))
                .thenThrow(new NotFoundException("Вещь не найдена"));

        assertThrows(NotFoundException.class, () -> itemService.updateItem(incomingItemDto, 99L, 1L));
    }

    @Test
    void searchItemsByText() {
    }

    @Test
    void addComment() {
    }
}