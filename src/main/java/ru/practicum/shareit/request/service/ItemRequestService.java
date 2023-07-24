package ru.practicum.shareit.request.service;
import ru.practicum.shareit.request.dto.IncomingItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;

public interface ItemRequestService {
    ItemRequestDto addItemReqest(IncomingItemRequestDto incomingItemRequestDto, Long userId);
}
