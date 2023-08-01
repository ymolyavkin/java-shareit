package ru.practicum.shareit.request.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.item.dto.ItemLastNextDto;
import ru.practicum.shareit.request.dto.IncomingItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.dto.ItemRequestWithAnswersDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.util.Constants.USER_ID_FROM_REQUEST;

@WebMvcTest(controllers = ItemRequestController.class)
@AutoConfigureMockMvc
class ItemRequestControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    MockMvc mockMvc;
    @MockBean
    private ItemRequestService itemRequestService;
    @InjectMocks
    private ItemRequestController itemRequestController;
    private ItemLastNextDto itemLastNextDto;
    private ItemRequest itemRequest;
    private IncomingItemRequestDto incomingItemRequestDto;
    private User requester;

    @BeforeEach
    void setUp() {
        requester = new User(1L, "Owner", "owner@email.ru");

        itemRequest = new ItemRequest();
        itemRequest.setId(1L);
        itemRequest.setRequester(requester);
        itemRequest.setCreated(LocalDateTime.now());
        itemRequest.setDescription("Description");

        itemLastNextDto = ItemLastNextDto.builder()
                .id(1L)
                .name("Name")
                .description("ItemDescription")
                .isAvailable(true)
                .build();
        incomingItemRequestDto = new IncomingItemRequestDto();
        incomingItemRequestDto.setDescription("Description");
    }


    @Test
    void addItemRequest() throws Exception {
        Long userId = 1L;
        ItemRequestResponseDto itemRequestResponseDto = ItemRequestMapper.mapToItemRequestResponseDto(itemRequest);

        when(itemRequestService.addItemRequest(incomingItemRequestDto, userId)).thenReturn(itemRequestResponseDto);

        String result = mockMvc.perform(post("/requests")
                        .header(USER_ID_FROM_REQUEST, 1)
                        .content(objectMapper.writeValueAsString(incomingItemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.ALL))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(itemRequestResponseDto), result);
    }

    @Test
    void addItemRequest_wrongInputData_throwsException() throws Exception {
        Long userId = 1L;
        incomingItemRequestDto.setDescription("");

        when(itemRequestService.addItemRequest(incomingItemRequestDto, userId))
                .thenThrow(new BadRequestException("Получены некорректные данные"));

        mockMvc.perform(post("/requests")
                        .header(USER_ID_FROM_REQUEST, 1)
                        .content(objectMapper.writeValueAsString(incomingItemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.ALL))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getItemRequestsByAuthor() throws Exception {
        Long userId = 1L;
        int from = 0;
        int size = 2;
        List<ItemRequestWithAnswersDto> expectedItemRequests = Collections.emptyList();
        when(itemRequestService.getItemRequestsByAuthor(userId, from, size))
                .thenReturn(expectedItemRequests);

        mockMvc.perform(get("/requests")
                        .header(USER_ID_FROM_REQUEST, 1))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void getItemRequestsByOther() throws Exception {
        Long userId = 1L;
        int from = 0;
        int size = 2;
        List<ItemRequestWithAnswersDto> expectedItemRequests = Collections.emptyList();
        when(itemRequestService.getItemRequestsByOther(userId, from, size))
                .thenReturn(expectedItemRequests);

        mockMvc.perform(get("/requests/all")
                        .header(USER_ID_FROM_REQUEST, 1))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void getItemRequestById() throws Exception {
        Long userId = 1L;
        Long requestId = 1L;

        ItemRequestWithAnswersDto expectedItemRequest = ItemRequestWithAnswersDto
                .builder()
                .id(requestId)
                .description("Description")
                .created(LocalDateTime.now())
                .items(Collections.emptyList())
                .build();
        when(itemRequestService.getItemRequestById(userId, requestId))
                .thenReturn(expectedItemRequest);

        String result = mockMvc.perform(get("/requests/{requestId}", requestId)
                        .header(USER_ID_FROM_REQUEST, 1))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(expectedItemRequest), result);
    }
}