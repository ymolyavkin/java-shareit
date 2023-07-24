package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.dto.IncomingItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.validator.Marker;

import static ru.practicum.shareit.util.Constants.USER_ID_FROM_REQUEST;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping(consumes = "application/json")
    public ItemRequestDto addItemRequest(@RequestHeader(value = USER_ID_FROM_REQUEST, defaultValue = "-1") Long userId,
                                          @Validated({Marker.OnCreate.class})
                                          @RequestBody IncomingItemRequestDto incomingItemRequestDto,
                                          BindingResult errors) {
        //this is the validation barrier
        if (errors.hasErrors()) {
            throw new NotFoundException("My exception");
        }
        log.info("Получен запрос на добавление запроса на вещь");

        return itemRequestService.addItemReqest(incomingItemRequestDto, userId);
    }
}
