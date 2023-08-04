package ru.practicum.shareit.request.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class ItemRequestTest {
    ItemRequest itemRequest;
    ItemRequest itemRequestTwo;
    private User userOwner;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private final LocalDateTime created = LocalDateTime.parse("2023-01-14 11:04", formatter);

    @BeforeEach
    public void setUp() {
        userOwner = new User();
        userOwner.setEmail("userowner@email.ru");
        userOwner.setName("ownerName");
        userOwner.setId(2L);
        itemRequest = new ItemRequest();
        itemRequest.setId(1L);
        itemRequest.setDescription("Description");
        itemRequest.setRequester(userOwner);
        itemRequest.setCreated(created);
        itemRequestTwo = new ItemRequest();
        itemRequestTwo.setId(1L);
        itemRequestTwo.setDescription("Description");
        itemRequestTwo.setRequester(userOwner);
        itemRequestTwo.setCreated(created);
    }

    @Test
    void testEqualsThisEqual() {
        ItemRequest itemRequest1 = itemRequest;
        ItemRequest itemRequest2 = itemRequest;

        assertTrue(itemRequest1.equals(itemRequest2));
    }

    @Test
    void testEqualsIsNull() {
        ItemRequest itemRequest1 = null;
        ItemRequest itemRequest2 = itemRequest;

        assertFalse(itemRequest2.equals(itemRequest1));
    }

    @Test
    void testEqualsDifferentClass() {
        User user1 = userOwner;
        ItemRequest itemrequest2 = itemRequest;

        assertFalse(itemrequest2.equals(user1));
    }

    @Test
    void testEqualsOneItem() {
        ItemRequest itemRequest1 = itemRequest;
        ItemRequest itemRequest2 = (ItemRequest) itemRequestTwo;

        assertEquals(itemRequest1.getId(), itemRequest2.getId());
        assertTrue(itemRequest2.equals(itemRequest1));
    }

}