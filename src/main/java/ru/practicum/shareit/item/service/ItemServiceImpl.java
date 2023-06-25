package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NoneXSharerUserIdException;
import ru.practicum.shareit.item.dto.IncomingItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;

import java.util.List;
import java.util.Optional;

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
    public List<Item> getItems(Long userId) {
        return itemStorage.getItems(userId);
    }

    @Override
    public Item addItem(IncomingItemDto incomingItemDto) {
        if (incomingItemDto.getOwnerId().equals(-1)) {
            throw new NoneXSharerUserIdException("Не указан владелец вещи");
        }
        return itemStorage.addItem(ItemMapper.toItem(incomingItemDto));
    }

    @Override
    public Item updateItem(Item item, Long itemId, Long userId) {
        return itemStorage.updateItem(item, itemId, userId);
    }

    @Override
    public Optional<Item> getItemById(Long id) {
        return itemStorage.getItemById(id);
    }

    @Override
    public List<Item> searchItems(String keyword) {
        return itemStorage.searchItems(keyword);
    }
}
