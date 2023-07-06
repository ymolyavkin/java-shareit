package ru.practicum.shareit.item.service;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.IncomingItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
   /* List<Item> getItems();

    List<Item> getItems(Long userId);

    Item addItem(IncomingItemDto incomingItemDto);

    Item updateItem(Item item, Long itemId, Long userId);

    Item getItemById(Long id);

    List<Item> searchItems(String keyword);*/

    List<Item> getAll();


    Item getItemById(Long id);

    @Transactional
    ItemDto saveItem(IncomingItemDto incomingItemDto);
}
