package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.*;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static org.springframework.data.domain.Sort.Direction.DESC;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemRequestRepository itemRequestRepository;

    public ItemRequestResponseDto addItemRequest(IncomingItemRequestDto incomingItemRequestDto, Long requesterId) {
        User requester = userRepository.findById(requesterId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с id %d не найден", requesterId)));
        ItemRequest itemRequest = itemRequestRepository.save(ItemRequestMapper.mapToItemRequest(incomingItemRequestDto, requester));

        return ItemRequestMapper.mapToItemRequestResponseDto(itemRequest);
    }

    @Override
    public List<ItemRequestWithAnswersDto> getItemRequestsByAuthor(Long requesterId, int from, int size) {
        User requester = userRepository.findById(requesterId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с id %d не найден", requesterId)));
        Pageable firstPageWithTwoElements = PageRequest.of(from, size);
        Page<ItemRequest> requests = itemRequestRepository.findAllByRequesterIdOrderByCreatedDesc(requesterId, firstPageWithTwoElements);

        List<ItemRequest> itemRequests = requests.getContent();
        List<Long> requestIdsList = itemRequests.stream().map(request -> request.getId()).collect(toList());
        Map<Long, List<Item>> itemsByRequest = itemRepository.findByRequestIdIn(requestIdsList, Sort.by(DESC, "id"))
                .stream()
                .collect(groupingBy(Item::getRequestId, toList()));

        return itemRequests
                .stream()
                .map(itemRequest -> ItemRequestMapper.mapToItemRequestAnswerDto(itemRequest, getAnswersToRequestWithoutQueryDB(itemRequest, itemsByRequest)))
                .collect(toList());
    }

    //private List<ItemAnswerToRequestDto> getAnswersToRequestWithoutQueryDB(ItemRequest itemRequest, Map<Long, List<Item>> itemsByRequest) {
    private List<ItemAnswerToRequestDto> getAnswersToRequestWithoutQueryDB(ItemRequest itemRequest, Map<Long, List<Item>> itemsByRequest) {
        List<Item> items;
        if (itemsByRequest.size() > 0) {
            items = itemsByRequest.get(itemRequest.getId());
        } else {
            items = Collections.emptyList();
        }
        return items.stream().map(ItemRequestMapper::mapToItemAnswerToRequestDto).collect(toList());
    }

    @Override
    public List<ItemRequestWithAnswersDto> getItemRequestsByOther(Long requesterId, int from, int size) {
        User requester = userRepository.findById(requesterId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с id %d не найден", requesterId)));
        Pageable pageable = PageRequest.of(from, size);
        Page<ItemRequest> requests = itemRequestRepository.findAllByRequesterIdNotOrderByCreatedDesc(requesterId, pageable);
        List<ItemRequest> itemRequests = requests.getContent();
        List<Long> requestIdsList = itemRequests.stream().map(request -> request.getId()).collect(toList());

        Map<Long, List<Item>> itemsByRequest = itemRepository.findByRequestIdIn(requestIdsList, Sort.by(DESC, "id"))
                .stream()
                .collect(groupingBy(Item::getRequestId, toList()));

        return itemRequests
                .stream()
                .map(itemRequest -> ItemRequestMapper.mapToItemRequestAnswerDto(itemRequest,
                        getAnswersToRequestWithoutQueryDB(itemRequest, itemsByRequest)))
                .collect(toList());
    }

    @Override
    public ItemRequestWithAnswersDto getItemRequestById(Long userId, Long requestId) {
        User requester = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с id %d не найден", userId)));
        ItemRequest itemRequest = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException(String.format("Запрос с id %d не найден", requestId)));
        return ItemRequestMapper.mapToItemRequestAnswerDto(itemRequest, getAnswersToRequest(itemRequest));
    }

    private List<ItemAnswerToRequestDto> getAnswersToRequest(ItemRequest itemRequest) {
        List<Item> items = itemRepository.findAllByRequestId(itemRequest.getId());
        return items.stream().map(ItemRequestMapper::mapToItemAnswerToRequestDto).collect(toList());
    }
}
