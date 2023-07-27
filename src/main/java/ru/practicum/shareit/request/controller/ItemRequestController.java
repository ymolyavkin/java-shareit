package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.request.dto.IncomingItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithAnswersDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.validator.Marker;

import javax.validation.constraints.Min;
import java.util.List;

import static ru.practicum.shareit.util.Constants.USER_ID_FROM_REQUEST;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping(consumes = "application/json")
    public ItemRequestResponseDto addItemRequest(@RequestHeader(value = USER_ID_FROM_REQUEST, defaultValue = "-1") Long userId,
                                                 @Validated({Marker.OnCreate.class})
                                                 @RequestBody IncomingItemRequestDto incomingItemRequestDto,
                                                 BindingResult errors) {
        if (errors.hasErrors()) {
            throw new BadRequestException("Получены некорректные данные");
        }
        log.info("Получен запрос на добавление запроса на вещь");

        return itemRequestService.addItemRequest(incomingItemRequestDto, userId);
    }

    @GetMapping
    public List<ItemRequestWithAnswersDto> getItemRequestsByAuthor(
            @RequestHeader(value = USER_ID_FROM_REQUEST, defaultValue = "-1") Long userId,
            @RequestParam(name = "from", defaultValue = "0") int from,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        log.info("Получен запрос на выдачу всех запросов пользователя с id = {}", userId);

        return itemRequestService.getItemRequestsByAuthor(userId, from, size);
    }

    @GetMapping("/all")
    public List<ItemRequestWithAnswersDto> getItemRequestsByOther(
            @Validated
            @RequestHeader(value = USER_ID_FROM_REQUEST, defaultValue = "-1") Long userId,
            @RequestParam(name = "from", defaultValue = "0", required = false) @Min(0) Integer from,
            @RequestParam(name = "size", defaultValue = "10", required = false) @Min(1) Integer size) {
        log.info("Получен запрос на выдачу всех запросов пользователя с id = {}", userId);

        return itemRequestService.getItemRequestsByOther(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public List<ItemRequestWithAnswersDto> getItemRequestById(
            @RequestHeader(value = USER_ID_FROM_REQUEST, defaultValue = "-1") Long userId,
            @PathVariable Long requestId) {
        log.info("Получен запрос на выдачу всех запросов пользователя с id = {}", userId);

        return itemRequestService.getItemOneRequests(userId, requestId);
    }
}
