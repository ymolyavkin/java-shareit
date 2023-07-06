package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.IncomingItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
 //   private final ItemStorage itemStorage;
    private final ItemRepository itemRepository;
    @Override
    public List<Item> getAll() {
        return itemRepository.findAll();
    }
    @Override
    public Item getItemById(Long id) {
        return itemRepository.getReferenceById(id);
    }
    @Transactional
    @Override
    public ItemDto saveItem(IncomingItemDto incomingItemDto) {
        Item item = itemRepository.save(ItemMapper.toItem(incomingItemDto));
        return ItemMapper.toItemDto(item);
    }
   /* @Override
    public List<Item> getItems() {
        return itemStorage.getItems();
    }

    @Override
    public List<Item> getItems(Long userId) {
        return itemStorage.getItems(userId);
    }

    @Override
    public Item addItem(IncomingItemDto incomingItemDto) {
        if (incomingItemDto.getOwnerId().equals(-1L)) {
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
    }*/
}
