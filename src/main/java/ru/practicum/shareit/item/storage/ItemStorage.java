package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemStorage {
    List<Item> getItems();

    List<Item> getItems(Long userId);

    Optional<Item> getItemById(Long id);

    Item addItem(Item item);

    Item updateItem(Item item, Long itemId, Long userId);

    void deleteItemById(long id);

    Item getItemById(long itemId);

    List<Item> searchItems(String keyword);
}
