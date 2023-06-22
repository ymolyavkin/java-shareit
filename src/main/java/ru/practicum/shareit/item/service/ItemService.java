package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    List<Item> getItems();

    Item addItem(Item item);

    Item updateItem(Item item, Long id);
}
