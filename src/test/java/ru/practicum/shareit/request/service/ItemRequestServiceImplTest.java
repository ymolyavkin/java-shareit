package ru.practicum.shareit.request.service;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.IncomingItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.IncomingItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.dto.IncomingUserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceImplTest {
    @Mock
    private ItemRequestRepository itemRequestRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserService userService;
    @InjectMocks
    private ItemRequestServiceImpl itemRequestService;
    private final EasyRandom easyRandom = new EasyRandom();
    private ItemDto itemDto;
    private IncomingUserDto incomingOwner;
    private IncomingUserDto incomingRequester;
    private User userOwner;
    private User userRequester;
    private IncomingItemDto incomingItemDto;
    private IncomingItemRequestDto incomingItemRequestDto;
    private ItemRequest itemRequest;
    private Long requesterId = 1L;
    private Long ownerId = 1L;
    private Long itemRequestId = 1L;
    private int from = 0;
    private int size = 10;


    @BeforeEach
    void setUp() {
        incomingOwner = new IncomingUserDto("Owner", "email@email.ru");
        incomingRequester = new IncomingUserDto("Requester", "email@reqmail.ru");
        userOwner = UserMapper.mapToUser(incomingOwner);
        userRequester = UserMapper.mapToUser(incomingRequester);
        incomingItemDto = new IncomingItemDto();
        incomingItemDto.setName("ItemName");
        incomingItemDto.setDescription("DescriptionItem");
        incomingItemDto.setAvailable(true);
        incomingItemDto.setOwnerId(ownerId);
        incomingItemRequestDto = new IncomingItemRequestDto();
        incomingItemRequestDto.setDescription("incomingItemRequestDtoDescription");
        itemRequest = ItemRequestMapper.mapToItemRequest(incomingItemRequestDto, userRequester);
        itemRequest.setId(itemRequestId);
    }

    @Test
    void addItemRequest_whenRequesterNotFound_thenNotSaveItemRequest() {
        IncomingUserDto incomingUserDto = easyRandom.nextObject(IncomingUserDto.class);
        User requester = UserMapper.mapToUser(incomingUserDto);
        IncomingItemRequestDto incomingItemRequestDto = easyRandom.nextObject(IncomingItemRequestDto.class);
        ItemRequest itemRequestToSave = ItemRequestMapper.mapToItemRequest(incomingItemRequestDto, requester);
        doThrow(NotFoundException.class).when(userRepository).findById(99L);

        assertThrows(NotFoundException.class, () -> itemRequestService.addItemRequest(incomingItemRequestDto, 99L));

        verify(itemRequestRepository, never()).save(itemRequestToSave);
        verify(itemRequestRepository, times(0)).save(itemRequestToSave);
        verify(itemRequestRepository, atMost(5)).save(itemRequestToSave);
    }

    @Test
    void getItemRequestsByAuthor() {
    }

    @Test
    void getItemRequestsByOther() {
    }

    @Test
    void getItemRequestById() {
    }

    @Test
    public void addItemRequestTest() {
        System.out.println();
        when(userRepository.findById(requesterId)).thenReturn(Optional.of(userRequester));
        when(itemRequestRepository.save(any())).thenReturn(itemRequest);
        ItemRequestResponseDto actualNewRequest = itemRequestService.addItemRequest(incomingItemRequestDto, requesterId);

        assertNotNull(actualNewRequest);
        assertEquals(incomingItemRequestDto.getDescription(), actualNewRequest.getDescription());
        verify(itemRequestRepository, times(1)).save(any());
    }

    @Test
    void createRequestWithWrongUserIdTest() {
        Long requesterId = 99L;
        when(userRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> itemRequestService.addItemRequest(incomingItemRequestDto, requesterId));
        verify(itemRequestRepository, never()).save(any());
    }

    @Test
    void findByRequesterIdWithWrongUserTest() {
        Long requesterId = 99L;
        when(userRepository.findById(requesterId)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> itemRequestService.getItemRequestsByAuthor(requesterId, from, size));

        verify(itemRequestRepository, never()).findAllByRequesterIdOrderByCreatedDesc(anyLong(), any());
    }

    @Test
    void getRequestByIdWithWrongRequesterIdTest() {
        Long requesterId = 99L;
        when(userRepository.findById(requesterId)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> itemRequestService.getItemRequestById(requesterId, itemRequestId));

        verify(itemRequestRepository, never()).findById(anyLong());
    }
}