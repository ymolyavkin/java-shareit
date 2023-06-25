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
import ru.practicum.shareit.util.Converter;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
        if (!userId.equals(-1L)) {
            return itemService.getItems(userId)
                    .stream()
                    .map(ItemMapper::toItemDto)
                    .collect(Collectors.toList());
        } else {
            return itemService.getItems()
                    .stream()
                    .map(ItemMapper::toItemDto)
                    .collect(Collectors.toList());
        }
    }

    @ExceptionHandler(UnsatisfiedServletRequestParameterException.class)
    @PostMapping
    public Item addItem(@RequestHeader(value = USER_ID_FROM_REQUEST, defaultValue = "-1") Long userId,
                        @Valid @RequestBody IncomingItemDto incomingItemDto) {
        if (userId.equals(-1L)) {
            throw new NoneXSharerUserIdException("Не указан владелец вещи");
        }
        incomingItemDto.setOwnerId(userId);
        return itemService.addItem(incomingItemDto);
    }

    @PatchMapping(value = "/{itemId}", consumes = "application/json")
    public Item updateItem(@RequestHeader(value = USER_ID_FROM_REQUEST, defaultValue = "-1") Long userId,
                           @RequestBody Item item,
                           @PathVariable Long itemId) {
        if (userId.equals(-1L)) {
            throw new NoneXSharerUserIdException("Не указан владелец вещи");
        }
        return itemService.updateItem(item, itemId, userId);
    }

    @GetMapping("/{id}")
    public ItemDto getItemById(@PathVariable Long id) {
        Item item = itemService.getItemById(id).get();
        return ItemMapper.toItemDto(item);
    }


    @GetMapping("/search")
    public List<Item> searchItems(@RequestParam String text) {
        if (text.isBlank()) {
            return new ArrayList<>(0);
        }
        return itemService.searchItems(text);
    }
}
