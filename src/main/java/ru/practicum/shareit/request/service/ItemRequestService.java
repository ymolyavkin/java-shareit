package ru.practicum.shareit.request.service;
import ru.practicum.shareit.request.dto.IncomingItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestAnswerDto;

public interface ItemRequestService {
    ItemRequestAnswerDto addItemReqest(IncomingItemRequestDto incomingItemRequestDto, Long userId);
}
