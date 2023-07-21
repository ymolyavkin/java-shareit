package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.IncomingItemRequestDto;
import ru.practicum.shareit.user.model.User;

public interface ItemRequestService {
    User addItemReqest(IncomingItemRequestDto incomingItemRequestDto, Long userId);
}
