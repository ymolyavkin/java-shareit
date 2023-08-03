package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.IncomingItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dto.ItemRequestWithAnswersDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dto.IncomingUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;

import javax.transaction.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest(properties = {"db.name=test"})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemRequestServiceImpIntegrationlTest {
    @Mock
    private final UserRepository userRepository;
    private final ItemRequestService itemRequestService;
    private final UserService userService;
    private final ItemService itemService;
    private ItemDto itemDto;
    //  private ItemShortDto itemShortDto;
    private ItemDto savedItem;
    private IncomingUserDto incomingOwner;
    private IncomingUserDto incomingRequester;
    private User userOwner;
    private User userRequester;
    private UserDto savedRequester;
    private IncomingItemDto incomingItemDto;
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
        incomingItemDto.setOwnerId(ownerId);
//                .description("Item description")
//                .available(true)
//                .owner(new ItemDto.Owner(userOwner.getId(), userOwner.getName(), userOwner.getEmail()))
//                .build();
//        itemShortDto = ItemShortDto.builder()
//                .id(itemDto.getId())
//                .name(itemDto.getName())
//                .description(itemDto.getDescription())
//                .requestId(itemDto.getRequestId())
//                .available(itemDto.getAvailable())
//                .build();
    }

    @Test
    void getItemRequestsByAuthor() {

        int from = 0;
        int size = 10;
        Mockito.when(userRepository.findById(requesterId))
                .thenThrow(new NotFoundException(String.format("Пользователь с id %d не найден", requesterId)));
        assertThrows(NotFoundException.class, () -> itemRequestService.getItemRequestsByAuthor(requesterId, from, size));


//        UserDto savedOwner = userService.createUser(userOwner);
//        savedRequestor = userService.createUser(userRequestor);
//        savedItem = itemService.createItem(itemShortDto, savedOwner.getId());
        itemDto = itemService.addItem(incomingItemDto);
//        ItemRequest addItemRequestDto = ItemRequest.builder()
//                .description("Description")
//                .requester(userDtotoUser(savedRequestor))
//                .created(Timestamp.valueOf(LocalDateTime.now()))
//                .build();
//        savedRequest = itemRequestService.createRequest(savedRequestor.getId(), itemRequestToItemRequestShortDto(addItemRequestDto));
//        List<ItemRequestDto> requestsList = itemRequestService.getAllRequestsByRequester(savedRequestor.getId(), 0, 10);

        List<ItemRequestWithAnswersDto> requestsList = itemRequestService.getItemRequestsByAuthor(requesterId, from, size);
//        assertEquals(1, requestsList.size());
//        assertEquals(savedRequest.getId(), requestsList.get(0).getId());
//        assertEquals(savedRequest.getDescription(), requestsList.get(0).getDescription());
    }
}
/*
public List<ItemRequestWithAnswersDto> getItemRequestsByAuthor(Long requesterId, int from, int size) {
        User requester = userRepository.findById(requesterId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с id %d не найден", requesterId)));
        Pageable firstPageWithTwoElements = PageRequest.of(from, size);
        Page<ItemRequest> requests = itemRequestRepository.findAllByRequesterIdOrderByCreatedDesc(requesterId, firstPageWithTwoElements);
        List<ItemRequest> itemRequests = requests.getContent();
        return itemRequests
                .stream()
                .map(itemRequest -> ItemRequestMapper.mapToItemRequestAnswerDto(itemRequest, getAnswersToRequest(itemRequest)))
                .collect(Collectors.toList());
 */