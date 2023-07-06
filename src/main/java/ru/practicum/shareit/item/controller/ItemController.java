package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.UnsatisfiedServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.NoneXSharerUserIdException;
import ru.practicum.shareit.item.dto.IncomingItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
     return  null;
    }

    @ExceptionHandler(UnsatisfiedServletRequestParameterException.class)
    @PostMapping
    public Item addItem(@RequestHeader(value = USER_ID_FROM_REQUEST, defaultValue = "-1") Long userId,
                        @Valid @RequestBody IncomingItemDto incomingItemDto) {
        log.info("Получен запрос на добавление вещи пользователя с id = {}", userId);
        if (userId.equals(-1L)) {
            throw new NoneXSharerUserIdException("Не указан владелец вещи");
        }
        incomingItemDto.setOwnerId(userId);
       // return itemService.addItem(incomingItemDto);
       return null;
    }

    @PatchMapping(value = "/{itemId}", consumes = "application/json")
    public Item updateItem(@RequestHeader(value = USER_ID_FROM_REQUEST, defaultValue = "-1") Long userId,
                           @RequestBody Item item,
                           @PathVariable Long itemId) {
        log.info("Получен запрос на обновление вещи пользователя с id = {}", userId);
        if (userId.equals(-1L)) {
            throw new NoneXSharerUserIdException("Не указан владелец вещи");
        }
       // return itemService.updateItem(item, itemId, userId);
        return null;
    }

    @GetMapping("/{id}")
    public ItemDto getItemById(@PathVariable Long id) {
        log.info("Получен запрос на выдачу вещи с id = {}", id);
        Item item = itemService.getItemById(id);
        return ItemMapper.toItemDto(item);
    }

    @GetMapping("/search")
    public List<Item> searchItems(@RequestParam String text) {
        log.info("Получен запрос на поиск вещей по ключевому слову \'{}\'", text);
        if (text.isBlank()) {
            return new ArrayList<>(0);
        }
        //return itemService.searchItems(text);
        return null;
    }
}
