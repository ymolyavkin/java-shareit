package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.dto.IncomingItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestAnswerDto;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    @Override
    public ItemRequestAnswerDto addItemReqest(IncomingItemRequestDto incomingItemRequestDto, Long userId) {
        return null;
    }
}
