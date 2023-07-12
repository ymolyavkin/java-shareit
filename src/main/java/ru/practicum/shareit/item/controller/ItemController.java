package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.UnsatisfiedServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.NoneXSharerUserIdException;
import ru.practicum.shareit.item.dto.IncomingItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
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
    public List<ItemDto> getItems(@RequestHeader(value = USER_ID_FROM_REQUEST, defaultValue = "-1") Long userId) {
        log.info("Получен запрос на выдачу вещей пользователя с id = {}", userId);

        return itemService.getItemsByUser(userId);
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

    @GetMapping("/{id}")
    public ItemDto getItemById(@PathVariable Long id) {
        log.info("Получен запрос на выдачу вещи с id = {}", id);

        return  itemService.getItemById(id);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam String text) {
        log.info("Получен запрос на поиск вещей по ключевому слову \'{}\'", text);
        if (text.isBlank()) {
            return new ArrayList<>(0);
        }
        return itemService.searchItemsByText(text);
    }
}
