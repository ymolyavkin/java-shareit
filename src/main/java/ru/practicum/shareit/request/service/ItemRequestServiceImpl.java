package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.dto.IncomingItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    // private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemRequestRepository itemRequestRepository;

    public ItemRequestResponseDto addItemRequest(IncomingItemRequestDto incomingItemRequestDto, Long requesterId) {
        User requester = userRepository.findById(requesterId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с id %d не найден", requesterId)));
        ItemRequest itemRequest = itemRequestRepository.save(ItemRequestMapper.mapToItemRequest(incomingItemRequestDto, requester));

        return ItemRequestMapper.mapToItemRequestResponseDto(itemRequest);
    }

    @Override
    public List<ItemRequestResponseDto> getItemRequestsByAuthor(Long userId, int from, int size) {
        Pageable firstPageWithTwoElements = PageRequest.of(from, size);
        Page<ItemRequest> requests = itemRequestRepository.findAllByRequesterIdOrderByCreatedDesc(userId, firstPageWithTwoElements);
        List<ItemRequest> itemRequests = requests.getContent();
        return itemRequests.stream().map(ItemRequestMapper::mapToItemRequestResponseDto).collect(Collectors.toList());
    }
}
