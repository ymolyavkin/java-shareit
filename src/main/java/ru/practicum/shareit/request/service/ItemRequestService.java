package ru.practicum.shareit.request.service;
import ru.practicum.shareit.request.dto.IncomingItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithAnswersDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestResponseDto addItemRequest(IncomingItemRequestDto incomingItemRequestDto, Long requesterId);

    List<ItemRequestWithAnswersDto> getItemRequestsByAuthor(Long userId, int from, int size);

    List<ItemRequestWithAnswersDto> getItemOneRequests(Long userId, Long requestId);
}
