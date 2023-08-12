package ru.practicum.shareitserver.request.controller;

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

import static ru.practicum.shareit.util.Constants.REQUEST_GIVE_ALL_USER_QUERIES;
import static ru.practicum.shareit.util.Constants.USER_ID_FROM_REQUEST;

@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping(consumes = "application/json")
    public ItemRequestResponseDto addItemRequest(@RequestHeader(value = USER_ID_FROM_REQUEST) Long userId,
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
            @RequestHeader(value = USER_ID_FROM_REQUEST) Long userId,
            @RequestParam(name = "from", defaultValue = "0") int from,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        log.info(REQUEST_GIVE_ALL_USER_QUERIES, userId);

        return itemRequestService.getItemRequestsByAuthor(userId, from, size);
    }

    @GetMapping("/all")
    public List<ItemRequestWithAnswersDto> getItemRequestsByOther(
            @RequestHeader(value = USER_ID_FROM_REQUEST) Long userId,
            @RequestParam(name = "from", defaultValue = "0") @Min(0) Integer from,
            @RequestParam(name = "size", defaultValue = "10") @Min(1) Integer size) {
        log.info(REQUEST_GIVE_ALL_USER_QUERIES, userId);

        return itemRequestService.getItemRequestsByOther(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ItemRequestWithAnswersDto getItemRequestById(
            @RequestHeader(value = USER_ID_FROM_REQUEST) Long userId,
            @PathVariable Long requestId) {
        log.info(REQUEST_GIVE_ALL_USER_QUERIES, userId);

        return itemRequestService.getItemRequestById(userId, requestId);
    }
}
