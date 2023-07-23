package ru.practicum.shareit.request.service;

import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.request.dto.IncomingItemRequestDto;

public interface ItemRequestService {
    ItemResponseDto addItemReqest(IncomingItemRequestDto incomingItemRequestDto, Long userId);
}
