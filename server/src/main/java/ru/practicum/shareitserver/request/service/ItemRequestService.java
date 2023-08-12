package ru.practicum.shareitserver.request.service;

import ru.practicum.shareitserver.request.dto.IncomingItemRequestDto;
import ru.practicum.shareitserver.request.dto.ItemRequestResponseDto;
import ru.practicum.shareitserver.request.dto.ItemRequestWithAnswersDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestResponseDto addItemRequest(IncomingItemRequestDto incomingItemRequestDto, Long requesterId);

    List<ItemRequestWithAnswersDto> getItemRequestsByAuthor(Long requesterId, int from, int size);

    List<ItemRequestWithAnswersDto> getItemRequestsByOther(Long userId, int from, int size);

    ItemRequestWithAnswersDto getItemRequestById(Long userId, Long requestId);
}
