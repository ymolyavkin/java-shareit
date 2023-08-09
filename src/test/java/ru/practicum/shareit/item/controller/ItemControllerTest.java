package ru.practicum.shareit.item.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import ru.practicum.shareit.item.comment.Comment;

import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.dto.CommentMapper;
import ru.practicum.shareit.item.comment.dto.IncomingCommentDto;
import ru.practicum.shareit.item.dto.IncomingItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemLastNextDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.practicum.shareit.util.Constants.USER_ID_FROM_REQUEST;

@WebMvcTest(controllers = ItemController.class)
@AutoConfigureMockMvc
class ItemControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    MockMvc mockMvc;
    @MockBean
    private ItemService itemService;
    private ItemLastNextDto itemLastNextDto;
    private IncomingItemDto incomingItemDto;
    private User owner;
    private Item item;

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
        Long userId = 1L;
        incomingItemDto.setOwnerId(userId);
        incomingItemDto.setAvailable(true);

        owner = new User(1L, "Owner", "owner@email.ru");
        item = ItemMapper.mapToItem(incomingItemDto, owner);
        item.setId(1L);
    }

    @Test
    void getItems() throws Exception {
        List<ItemLastNextDto> expectedItems = List.of(itemLastNextDto);
        when(itemService.getItemsLastNextBookingByUser(1L, 0, 2))
                .thenReturn(expectedItems);

        mockMvc.perform(get("/items")
                        .header(USER_ID_FROM_REQUEST, 1))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void addItem() throws Exception {
        Item itemToAdd = ItemMapper.mapToItem(incomingItemDto, owner);
        itemToAdd.setId(1L);
        itemToAdd.setAvailable(true);
        ItemDto itemDto = ItemMapper.mapToItemDto(itemToAdd);

        when(itemService.addItem(incomingItemDto)).thenReturn(itemDto);

        String result = mockMvc.perform(post("/items")
                        .header(USER_ID_FROM_REQUEST, 1)
                        .content(objectMapper.writeValueAsString(incomingItemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.ALL))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(itemDto), result);
    }

    @Test
    void updateItem() throws Exception {
        Long itemId = 1L;
        Long ownerId = owner.getId();
        Item itemToUpdate = ItemMapper.mapToItem(incomingItemDto, owner);
        itemToUpdate.setId(1L);
        itemToUpdate.setAvailable(true);
        ItemDto itemDto = ItemMapper.mapToItemDto(itemToUpdate);

        when(itemService.updateItem(incomingItemDto, itemId, ownerId)).thenReturn(itemDto);

        String result = mockMvc.perform(patch("/items/{itemId}", itemId)
                        .header(USER_ID_FROM_REQUEST, 1)
                        .content(objectMapper.writeValueAsString(incomingItemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.ALL))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(itemDto), result);
    }

    @Test
    void getItemById() throws Exception {
        Long itemId = 1L;
        Long userId = 1L;
        mockMvc.perform(get("/items/{id}", itemId)
                        .header(USER_ID_FROM_REQUEST, 1))
                .andDo(print())
                .andExpect(status().isOk());
        verify(itemService).getItemById(itemId, userId);
    }

    @Test
    void searchItems() throws Exception {
        when(itemService.searchItemsByText("", 0, 2)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/items")
                        .header(USER_ID_FROM_REQUEST, 1))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void searchItemsByEmptyText() throws Exception {
        when(itemService.searchItemsByText("", 0, 2)).thenReturn(Collections.emptyList());
        List<ItemDto> response = new ArrayList<>(0);
        mockMvc.perform(get("/items")
                        .header(USER_ID_FROM_REQUEST, 1)
                        .param("text", "")
                        .param("from", "0")
                        .param("size", "2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void searchItemsByText() throws Exception {
        List<ItemDto> response = List.of(ItemMapper.mapToItemDto(item));
        when(itemService.searchItemsByText("", 0, 2)).thenReturn(response);


        mockMvc.perform(get("/items")
                        .header(USER_ID_FROM_REQUEST, 1)
                        .param("text", "Name")
                        .param("from", "0")
                        .param("size", "2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void addComment() throws Exception {
        Long commentId = 1L;
        Long itemId = 1L;
        Long ownerId = owner.getId();
        IncomingCommentDto incomingCommentDto = new IncomingCommentDto();
        incomingCommentDto.setText("Text comment");

        Item item = ItemMapper.mapToItem(incomingItemDto, owner);
        item.setId(itemId);
        Comment comment = CommentMapper.mapToComment(incomingCommentDto, owner, item);
        comment.setId(commentId);
        CommentDto commentDto = CommentMapper.mapToCommentDto(comment);

        when(itemService.addComment(incomingCommentDto, ownerId, itemId)).thenReturn(commentDto);

        String result = mockMvc.perform(post("/items/{itemId}/comment", itemId)
                        .header(USER_ID_FROM_REQUEST, 1)
                        .content(objectMapper.writeValueAsString(incomingCommentDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.ALL))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(commentDto), result);
    }

    @Test
    void addComment_whenIncorrectX_Sharer_User_Id_thenThrown() throws Exception {
        Long itemId = 1L;
        IncomingCommentDto incomingCommentDto = new IncomingCommentDto();
        incomingCommentDto.setText("Text comment");
        mockMvc.perform(post("/items/{itemId}/comment", itemId)
                        .header("X-Sharer-User-Id", "")
                        .content(objectMapper.writeValueAsString(incomingCommentDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.ALL))
                .andExpect(status().isBadRequest());
    }

    @Test
    void addItem_whenWrongIncoming_thenThrown() throws Exception {
        incomingItemDto.setName("");

        mockMvc.perform(post("/items")
                        .header(USER_ID_FROM_REQUEST, 1)
                        .content(objectMapper.writeValueAsString(incomingItemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.ALL))
                .andExpect(status().isBadRequest());
    }
}