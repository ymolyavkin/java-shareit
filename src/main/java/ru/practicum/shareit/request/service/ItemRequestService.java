package ru.practicum.shareit.request.service;
import ru.practicum.shareit.request.dto.IncomingItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithAnswersDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestResponseDto addItemRequest(IncomingItemRequestDto incomingItemRequestDto, Long requesterId);

    List<ItemRequestWithAnswersDto> getItemRequestsByAuthor(Long requesterId, int from, int size);

    List<ItemRequestWithAnswersDto> getItemRequestsByOther(Long userId, int from, int size);

    ItemRequestWithAnswersDto getItemRequestById(Long userId, Long requestId);
}
