package ru.practicum.shareitgateway.item;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.UnsatisfiedServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.NoneXSharerUserIdException;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.dto.IncomingCommentDto;
import ru.practicum.shareit.item.dto.IncomingItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemLastNextDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.validator.Marker;
import ru.practicum.shareitgateway.item.dto.IncomingItemDto;

import javax.validation.constraints.Min;
import java.util.ArrayList;

import static ru.practicum.shareitgateway.util.Constants.USER_ID_FROM_REQUEST;


@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
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
    ResponseEntity<Object> addItem(@RequestHeader(value = USER_ID_FROM_REQUEST) Long userId,
                           @Validated({Marker.OnCreate.class}) @RequestBody IncomingItemDto incomingItemDto) {
        log.info("Получен запрос пользователя с id = {} на добавление вещи", userId);

      //  incomingItemDto.setOwnerId(userId);
        return itemClient.addItem(userId, incomingItemDto);
    }

    @PatchMapping(value = "/{itemId}", consumes = "application/json")
    public ItemDto updateItem(@RequestHeader(value = USER_ID_FROM_REQUEST) Long userId,
                              @Validated({Marker.OnUpdate.class}) @RequestBody IncomingItemDto incomingItemDto,
                              @PathVariable Long itemId) {
        log.info("Получен запрос на обновление вещи id = {} пользователя с id = {}", itemId, userId);

        return itemService.updateItem(incomingItemDto, itemId, userId);
    }

    @GetMapping("/{itemId}")
    public ItemLastNextDto getItemById(@RequestHeader(value = USER_ID_FROM_REQUEST) Long userId,
                                       @PathVariable Long itemId) {
        log.info("Получен запрос на выдачу вещи с id = {} пользователем с id = {}", itemId, userId);

        return itemService.getItemById(itemId, userId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam String text,
                                     @RequestParam(name = "from", defaultValue = "0") @Min(0) Integer from,
                                     @RequestParam(name = "size", defaultValue = "10") @Min(1) Integer size) {
        log.info("Получен запрос на поиск вещей по ключевому слову \'{}\'", text);
        if (text.isBlank()) {
            return new ArrayList<>(0);
        }
        return itemService.searchItemsByText(text, from, size);
    }

    @PostMapping(value = "/{itemId}/comment", consumes = "application/json")
    public CommentDto addComment(@RequestHeader(value = USER_ID_FROM_REQUEST) Long userId,
                                 @Validated({Marker.OnCreate.class}) @RequestBody IncomingCommentDto incomingCommentDto,
                                 @PathVariable Long itemId) {
        log.info("Получен запрос пользователя с id = {} на добавление комментария к вещи с id = {}", userId, itemId);
        if (userId.equals(-1L)) {
            throw new NoneXSharerUserIdException("Не указан владелец вещи");
        }
        return itemService.addComment(incomingCommentDto, userId, itemId);
    }
}