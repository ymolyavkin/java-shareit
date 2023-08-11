package ru.practicum.shareitgateway.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareitgateway.exception.BadRequestException;
import ru.practicum.shareitgateway.request.dto.IncomingItemRequestDto;
import ru.practicum.shareitgateway.validator.Marker;

import javax.validation.constraints.Min;

import static ru.practicum.shareitgateway.util.Constants.REQUEST_GIVE_ALL_USER_QUERIES;
import static ru.practicum.shareitgateway.util.Constants.USER_ID_FROM_REQUEST;


@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    @PostMapping(consumes = "application/json")
    public ResponseEntity<Object> addItemRequest(@RequestHeader(value = USER_ID_FROM_REQUEST) Long userId,
                                                 @Validated({Marker.OnCreate.class})
                                                 @RequestBody IncomingItemRequestDto incomingItemRequestDto,
                                                 BindingResult errors) {
        if (errors.hasErrors()) {
            throw new BadRequestException("Получены некорректные данные");
        }
        log.info("Получен запрос на добавление запроса на вещь");

        return itemRequestClient.addItemRequest(userId, incomingItemRequestDto);
    }

    @GetMapping
    public ResponseEntity<Object> getItemRequestsByAuthor(
            @RequestHeader(value = USER_ID_FROM_REQUEST) Long userId) {
        log.info(REQUEST_GIVE_ALL_USER_QUERIES, userId);

        return itemRequestClient.getItemRequestsByAuthor(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getItemRequestsByOther(
            @RequestHeader(value = USER_ID_FROM_REQUEST) Long userId,
            @RequestParam(name = "from", defaultValue = "0") @Min(0) Integer from,
            @RequestParam(name = "size", defaultValue = "10") @Min(1) Integer size) {
        log.info("Получить список запросов, созданных другими пользователями", userId);

        return itemRequestClient.getItemRequestsByOther(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getItemRequestById(
            @RequestHeader(value = USER_ID_FROM_REQUEST) Long userId,
            @PathVariable Long requestId) {
        log.info(REQUEST_GIVE_ALL_USER_QUERIES, userId);

        return itemRequestClient.getItemRequestById(userId, requestId);
    }
}