package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.UnsatisfiedServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.CommentErrorException;
import ru.practicum.shareit.exception.NoneXSharerUserIdException;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.IncomingCommentDto;
import ru.practicum.shareit.item.dto.IncomingItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemLastNextDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

import static ru.practicum.shareit.util.Constants.USER_ID_FROM_REQUEST;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @GetMapping
   // public List<ItemDto> getItems(@RequestHeader(value = USER_ID_FROM_REQUEST, defaultValue = "-1") Long userId) {
        public List<ItemLastNextDto> getItems(@RequestHeader(value = USER_ID_FROM_REQUEST, defaultValue = "-1") Long userId) {
        log.info("Получен запрос на выдачу вещей пользователя с id = {}", userId);

      //  return itemService.getItemsByUser(userId);
        return itemService.getItemsLastNextBookingByUser(userId);
    }

    @ExceptionHandler(UnsatisfiedServletRequestParameterException.class)
    @PostMapping
    public ItemDto addItem(@RequestHeader(value = USER_ID_FROM_REQUEST, defaultValue = "-1") Long userId,
                           @Valid @RequestBody IncomingItemDto incomingItemDto) {
        log.info("Получен запрос пользователя с id = {} на добавление вещи", userId);
        if (userId.equals(-1L)) {
            throw new NoneXSharerUserIdException("Не указан владелец вещи");
        }
        incomingItemDto.setOwnerId(userId);
        return itemService.addItem(incomingItemDto);
    }

    @PatchMapping(value = "/{itemId}", consumes = "application/json")
    public ItemDto updateItem(@RequestHeader(value = USER_ID_FROM_REQUEST, defaultValue = "-1") Long userId,
                              @RequestBody IncomingItemDto incomingItemDto,
                              @PathVariable Long itemId) {
        log.info("Получен запрос на обновление вещи id = {} пользователя с id = {}", itemId, userId);
        if (userId.equals(-1L)) {
            throw new NoneXSharerUserIdException("Не указан владелец вещи");
        }
        return itemService.updateItem(incomingItemDto, itemId, userId);
    }

    @GetMapping("/{itemId}")
    //public ItemDto getItemById(@PathVariable Long id) {
    public ItemLastNextDto getItemById(@RequestHeader(value = USER_ID_FROM_REQUEST, defaultValue = "-1") Long userId,
                                       @PathVariable Long itemId) {
        log.info("Получен запрос на выдачу вещи с id = {} пользователем с id = {}", itemId, userId);

        //return itemService.getItemById(id);
        return itemService.getItemById(itemId, userId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam String text) {
        log.info("Получен запрос на поиск вещей по ключевому слову \'{}\'", text);
        if (text.isBlank()) {
            return new ArrayList<>(0);
        }
        return itemService.searchItemsByText(text);
    }

  //  @ExceptionHandler(CommentErrorException.class)
    @PostMapping(value = "/{itemId}/comment", consumes = "application/json")
    public CommentDto addComment(@RequestHeader(value = USER_ID_FROM_REQUEST, defaultValue = "-1") Long userId,
                                 @Valid @RequestBody IncomingCommentDto incomingCommentDto,
                                 @PathVariable Long itemId) {
        log.info("Получен запрос пользователя с id = {} на добавление комментария к вещи с id = {}", userId, itemId);
        if (userId.equals(-1L)) {
            throw new NoneXSharerUserIdException("Не указан владелец вещи");
        }
        return itemService.addComment(incomingCommentDto, userId, itemId);
    }
}
