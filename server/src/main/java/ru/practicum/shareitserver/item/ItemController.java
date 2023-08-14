package ru.practicum.shareitserver.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.UnsatisfiedServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareitserver.exception.NoneXSharerUserIdException;
import ru.practicum.shareitserver.item.comment.dto.CommentDto;
import ru.practicum.shareitserver.item.comment.dto.IncomingCommentDto;
import ru.practicum.shareitserver.item.dto.IncomingItemDto;
import ru.practicum.shareitserver.item.dto.ItemDto;
import ru.practicum.shareitserver.item.dto.ItemLastNextDto;
import ru.practicum.shareitserver.item.service.ItemService;

import java.util.ArrayList;
import java.util.List;

import static ru.practicum.shareitserver.util.Constants.USER_ID_FROM_REQUEST;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @GetMapping
    public List<ItemLastNextDto> getItems(@RequestHeader(value = USER_ID_FROM_REQUEST) Long userId,
                                          @RequestParam(name = "from", defaultValue = "0") Integer from,
                                          @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Получен запрос на выдачу вещей пользователя с id = {}", userId);

        return itemService.getItemsLastNextBookingByUser(userId, from, size);
    }

    @ExceptionHandler(UnsatisfiedServletRequestParameterException.class)
    @PostMapping
    public ItemDto addItem(@RequestHeader(value = USER_ID_FROM_REQUEST) Long userId,
                           @RequestBody IncomingItemDto incomingItemDto) {
        log.info("Получен запрос пользователя с id = {} на добавление вещи", userId);
        if (userId.equals(-1L)) {
            throw new NoneXSharerUserIdException("Не указан владелец вещи");
        }
        incomingItemDto.setOwnerId(userId);
        return itemService.addItem(incomingItemDto);
    }

    @PatchMapping(value = "/{itemId}", consumes = "application/json")
    public ItemDto updateItem(@RequestHeader(value = USER_ID_FROM_REQUEST) Long userId,
                              @RequestBody IncomingItemDto incomingItemDto,
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
                                     @RequestParam(name = "from", defaultValue = "0") Integer from,
                                     @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Получен запрос на поиск вещей по ключевому слову \'{}\'", text);
        if (text.isBlank()) {
            return new ArrayList<>(0);
        }
        return itemService.searchItemsByText(text, from, size);
    }

    @PostMapping(value = "/{itemId}/comment", consumes = "application/json")
    public CommentDto addComment(@RequestHeader(value = USER_ID_FROM_REQUEST) Long userId,
                                 @RequestBody IncomingCommentDto incomingCommentDto,
                                 @PathVariable Long itemId) {
        log.info("Получен запрос пользователя с id = {} на добавление комментария к вещи с id = {}", userId, itemId);
        if (userId.equals(-1L)) {
            throw new NoneXSharerUserIdException("Не указан владелец вещи");
        }
        return itemService.addComment(incomingCommentDto, userId, itemId);
    }
}