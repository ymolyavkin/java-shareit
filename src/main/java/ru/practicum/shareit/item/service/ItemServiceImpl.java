package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemStorage itemStorage;

    @Override
    public List<Item> getItems() {
        return itemStorage.getItems();
    }

    @Override
    public Item addItem(Item item) {
        return itemStorage.addItem(item);
    }

    @Override
    public Item updateItem(Item item) {
        return itemStorage.updateItem(item);
    }
}
