package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.IncomingItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dto.IncomingItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.dto.ItemRequestWithAnswersDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dto.IncomingUserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;

import javax.transaction.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemRequestServiceImpIntegrationTest {
    @Mock
    private final UserRepository userRepository;
    private final ItemRequestService itemRequestService;
    private final UserService userService;
    private final ItemService itemService;
    private ItemDto itemDto;
    private IncomingUserDto incomingOwner;
    private IncomingUserDto incomingRequester;
    private User userOwner;
    private User userRequester;
    private IncomingItemDto incomingItemDto;
    private IncomingItemRequestDto incomingItemRequestDto;
    private Long requesterId = 1L;
    private Long ownerId = 1L;

    @BeforeEach
    void setUp() {
        incomingOwner = new IncomingUserDto("Owner", "email@email.ru");
        incomingRequester = new IncomingUserDto("Requester", "email@reqmail.ru");
        userOwner = userService.addUser(incomingOwner);
        userRequester = userService.addUser(incomingRequester);
        incomingItemDto = new IncomingItemDto();
        incomingItemDto.setName("ItemName");
        incomingItemDto.setDescription("DescriptionItem");
        incomingItemDto.setAvailable(true);
        incomingItemDto.setOwnerId(ownerId);
        incomingItemRequestDto = new IncomingItemRequestDto();
        incomingItemRequestDto.setDescription("incomingItemRequestDtoDescription");
    }

    @Test
    void getItemRequestsByAuthor() {

        int from = 0;
        int size = 10;
        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenThrow(new NotFoundException(String.format("Пользователь с id %d не найден", requesterId)));
        assertThrows(NotFoundException.class, () -> itemRequestService.getItemRequestsByAuthor(99L, from, size));

        itemDto = itemService.addItem(incomingItemDto);
        ItemRequest itemRequest = ItemRequestMapper.mapToItemRequest(incomingItemRequestDto, userRequester);

        ItemRequestResponseDto savedItemRequestResponse = itemRequestService.addItemRequest(incomingItemRequestDto, requesterId);
        List<ItemRequestWithAnswersDto> requestsList = itemRequestService.getItemRequestsByAuthor(requesterId, from, size);
        assertEquals(1, requestsList.size());
        assertEquals(savedItemRequestResponse.getId(), requestsList.get(0).getId());
        assertEquals(savedItemRequestResponse.getDescription(), requestsList.get(0).getDescription());
    }
}