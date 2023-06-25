package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.IncomingItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemService {
    List<Item> getItems();

    List<Item> getItems(Long userId);

    //Item addItem(Item item);
    Item addItem(IncomingItemDto incomingItemDto);

    Item updateItem(Item item, Long itemId, Long userId);

    Optional<Item> getItemById(Long id);

    List<Item> searchItems(String keyword);
}
