package ru.practicum.shareit.request.service;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.dto.IncomingItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.dto.IncomingUserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceImplTest {
    @Mock
    private ItemRequestRepository itemRequestRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private ItemRequestServiceImpl itemRequestService;
    private final EasyRandom easyRandom = new EasyRandom();

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
}