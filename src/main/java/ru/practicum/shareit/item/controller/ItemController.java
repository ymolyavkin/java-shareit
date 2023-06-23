package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.NoneXSharerUserIdException;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.utility.Converter;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemMapper itemMapper;
    private final ItemService itemService;

    @GetMapping
    public List<Item> getItems(@RequestHeader Map<String, String> headers) {
        if (headers.containsKey("x-sharer-user-id")) {
            Long userId = Converter.stringToLong(headers.get("x-sharer-user-id"));
            return itemService.getItems(userId);
        } else {
            return itemService.getItems();
        }
    }

    @PostMapping
    public Item addItem(@RequestHeader Map<String, String> headers, @Valid @RequestBody Item item) {
        if (headers.containsKey("x-sharer-user-id")) {
            item.setOwnerId(Converter.stringToLong(headers.get("x-sharer-user-id")));
        } else {
            throw new NoneXSharerUserIdException("Не указан владелец вещи");
        }
        return itemService.addItem(item);
    }

    @PatchMapping(value = "/{itemId}", consumes = "application/json")
    public Item updateItem(@RequestHeader Map<String, String> headers,
                           @RequestBody Item item,
                           @PathVariable Long itemId) {
        Long userId;
        if (headers.containsKey("x-sharer-user-id")) {
           userId = Converter.stringToLong(headers.get("x-sharer-user-id"));
        } else {
            throw new NoneXSharerUserIdException("Не указан владелец вещи");
        }
        return itemService.updateItem(item, itemId, userId);
    }
    @GetMapping("/{id}")
    public Optional<Item> getItemById(@PathVariable Long id) {
        return itemService.getItemById(id);
    }
}
