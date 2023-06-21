package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    public List<Item> getItems() {
        return new ArrayList<>();
    }

    public Item addItem(Item item) {
        return item;
    }

    public Item updateItem(Item item) {
        return item;
    }
}
