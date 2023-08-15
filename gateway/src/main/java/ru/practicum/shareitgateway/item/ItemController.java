package ru.practicum.shareitgateway.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareitgateway.item.comment.dto.IncomingCommentDto;
import ru.practicum.shareitgateway.item.dto.IncomingItemDto;
import ru.practicum.shareitgateway.validator.Marker;

import javax.validation.constraints.Min;

import java.util.Collections;

import static ru.practicum.shareitgateway.util.Constants.USER_ID_FROM_REQUEST;


@Slf4j
@Validated
@RequiredArgsConstructor
@Controller
@RequestMapping("/items")
public class ItemController {
    private final ItemClient itemClient;

    @GetMapping
    public ResponseEntity<Object> getItemsByUser(@RequestHeader(value = USER_ID_FROM_REQUEST) Long userId,
                                                 @RequestParam(name = "from", defaultValue = "0") @Min(0) Integer from,
                                                 @RequestParam(name = "size", defaultValue = "10") @Min(1) Integer size) {
        log.info("Получен запрос на выдачу вещей пользователя с id = {}", userId);

        return itemClient.getItemsByUser(userId, from, size);
    }

    @PostMapping
    public ResponseEntity<Object> addItem(@RequestHeader(value = USER_ID_FROM_REQUEST) Long userId,
                                          @Validated({Marker.OnCreate.class}) @RequestBody IncomingItemDto incomingItemDto) {
        log.info("Получен запрос пользователя с id = {} на добавление вещи", userId);

        return itemClient.addItem(userId, incomingItemDto);
    }

    @PatchMapping(value = "/{itemId}", consumes = "application/json")
    public ResponseEntity<Object> updateItem(@RequestHeader(value = USER_ID_FROM_REQUEST) Long userId,
                                             @Validated({Marker.OnUpdate.class}) @RequestBody IncomingItemDto incomingItemDto,
                                             @PathVariable Long itemId) {
        log.info("Получен запрос на обновление вещи id = {} пользователя с id = {}", itemId, userId);

        return itemClient.updateItem(incomingItemDto, itemId, userId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(@RequestHeader(value = USER_ID_FROM_REQUEST) Long userId,
                                              @PathVariable Long itemId) {
        log.info("Получен запрос на выдачу вещи с id = {} пользователем с id = {}", itemId, userId);

        return itemClient.getItemById(itemId, userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItemsByText(@RequestParam String text,
                                                    @RequestHeader(USER_ID_FROM_REQUEST) Long userId,
                                                    @RequestParam(name = "from", defaultValue = "0") @Min(0) Integer from,
                                                    @RequestParam(name = "size", defaultValue = "10") @Min(1) Integer size) {
        log.info("Получен запрос на поиск вещей по ключевому слову \'{}\'", text);
        if (text.isBlank()) {
            return ResponseEntity.ok(Collections.emptyList());
        }
        return itemClient.searchItemsByText(userId, text, from, size);
    }

    @PostMapping(value = "/{itemId}/comment", consumes = "application/json")
    public ResponseEntity<Object> addComment(@RequestHeader(value = USER_ID_FROM_REQUEST) Long userId,
                                             @Validated({Marker.OnCreate.class}) @RequestBody IncomingCommentDto incomingCommentDto,
                                             @PathVariable Long itemId) {
        log.info("Получен запрос пользователя с id = {} на добавление комментария к вещи с id = {}", userId, itemId);

        return itemClient.addComment(userId, itemId, incomingCommentDto);
    }
}