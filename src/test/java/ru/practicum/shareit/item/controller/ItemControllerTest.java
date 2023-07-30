package ru.practicum.shareit.item.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import ru.practicum.shareit.item.dto.IncomingItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemLastNextDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.util.Constants.USER_ID_FROM_REQUEST;

@WebMvcTest(controllers = ItemController.class)
@AutoConfigureMockMvc
//@ExtendWith(MockitoExtension.class)
class ItemControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    MockMvc mockMvc;
    @MockBean
    private ItemService itemService;
    @InjectMocks
    private ItemController itemController;
    private ItemLastNextDto itemLastNextDto;
    private IncomingItemDto incomingItemDto;
    private User owner;

    @BeforeEach
    void setUp() {
        itemLastNextDto = ItemLastNextDto.builder()
                .id(1L)
                .name("Name")
                .description("ItemDescription")
                .isAvailable(true)
                .build();
        incomingItemDto = new IncomingItemDto();
        incomingItemDto.setName("Name");
        incomingItemDto.setDescription("Description");

        owner = new User(1L, "Owner", "owner@email.ru");
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
    void updateItem() throws Exception {
        Long itemId = 1L;
        Long ownerId = owner.getId();
        //incomingItemDto.setName(null);
        Item itemToUpdate = ItemMapper.mapToItem(incomingItemDto, owner);
        itemToUpdate.setId(1L);
        itemToUpdate.setAvailable(true);
        ItemDto itemDto = ItemMapper.mapToItemDto(itemToUpdate);

        when(itemService.updateItem(incomingItemDto, itemId, ownerId)).thenReturn(itemDto);

        String result = mockMvc.perform(patch("/items/{id}", ownerId)
                        .header(USER_ID_FROM_REQUEST, 1)
                       // .contentType("application/json")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(itemDto), result);
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