package ru.practicum.shareit.item.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.dto.ItemLastNextDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ItemControllerTest {
    @Mock
    private ItemService itemService;
    @InjectMocks
    private ItemController itemController;
    ItemLastNextDto itemLastNextDto;

    @BeforeEach
    void setUp() {
        itemLastNextDto = ItemLastNextDto.builder()
                .id(1L)
                .name("Name")
                .description("ItemDescription")
                .isAvailable(true)
                .build();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getItems() {
        List<ItemLastNextDto> expectedItems = List.of(itemLastNextDto);
        Mockito.when(itemService.getItemsLastNextBookingByUser(1L, 0, 2))
                .thenReturn(expectedItems);

        List<ItemLastNextDto> items = itemController.getItems(1L, 0, 2);

        assertEquals(items.size(), 1);
        assertEquals(expectedItems, items);
    }

    @Test
    void addItem() {
    }

    @Test
    void updateItem() {
    }

    @Test
    void getItemById() {
    }

    @Test
    void searchItems() {
    }

    @Test
    void addComment() {
    }
}