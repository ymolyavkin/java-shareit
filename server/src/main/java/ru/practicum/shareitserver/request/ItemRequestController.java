package ru.practicum.shareitserver.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareitserver.request.dto.IncomingItemRequestDto;
import ru.practicum.shareitserver.request.dto.ItemRequestResponseDto;
import ru.practicum.shareitserver.request.dto.ItemRequestWithAnswersDto;
import ru.practicum.shareitserver.request.service.ItemRequestService;

import java.util.List;

import static ru.practicum.shareitserver.util.Constants.REQUEST_GIVE_ALL_USER_QUERIES;
import static ru.practicum.shareitserver.util.Constants.USER_ID_FROM_REQUEST;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping(consumes = "application/json")
    public ItemRequestResponseDto addItemRequest(@RequestHeader(value = USER_ID_FROM_REQUEST) Long userId,
                                                 @RequestBody IncomingItemRequestDto incomingItemRequestDto) {

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
            @RequestParam(name = "from", defaultValue = "0") Integer from,
            @RequestParam(name = "size", defaultValue = "10") Integer size) {
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