package ru.practicum.shareit.item.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.IncomingItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.*;

class ItemTest {
    private Item item;
    private Item itemTwo;
    private User userOwner;
    private IncomingItemDto incomingItemDto;
    private IncomingItemDto incomingItemDtoTwo;

    @BeforeEach
    public void setUp() {
        userOwner = new User();
        userOwner.setEmail("userowner@email.ru");
        userOwner.setName("ownerName");
        userOwner.setId(2L);
        incomingItemDto = new IncomingItemDto();
        incomingItemDto.setName("ItemName");
        incomingItemDto.setDescription("DescriptionItem");
        incomingItemDto.setAvailable(true);
        incomingItemDto.setOwnerId(userOwner.getId());
        item = ItemMapper.mapToItem(incomingItemDto, userOwner);
        item.setId(1L);
        incomingItemDtoTwo = new IncomingItemDto();
        incomingItemDtoTwo.setName("ItemName");
        incomingItemDtoTwo.setDescription("DescriptionItem");
        incomingItemDtoTwo.setAvailable(true);
        incomingItemDtoTwo.setOwnerId(userOwner.getId());
        itemTwo = ItemMapper.mapToItem(incomingItemDtoTwo, userOwner);
        itemTwo.setId(1L);
    }

    @Test
    void testEqualsThisEqual() {
        Item item1 = item;
        Item item2 = item;

        assertTrue(item1.equals(item2));
    }

    @Test
    void testEqualsIsNull() {
        Item item1 = null;
        Item item2 = item;

        assertFalse(item2.equals(item1));
    }

    @Test
    void testEqualsDifferentClass() {
        User user1 = userOwner;
        Item item2 = item;

        assertFalse(item2.equals(user1));
    }

    @Test
    void testEqualsOneItem() {
        Item item1 = item;
        Item item2 = (Item) itemTwo;

        assertEquals(item1.getId(), item2.getId());
        assertEquals(item1.getName(), item2.getName());
        assertTrue(item2.equals(item1));
    }
}