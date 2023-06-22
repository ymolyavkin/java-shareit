package ru.practicum.shareit.item.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;
import ru.practicum.shareit.utility.Converter;

import java.util.*;

@Slf4j
@Repository
public class ItemStorageImpl implements ItemStorage {
    private long id;
    private Map<Long, Item> items;
    private final UserStorage userStorage;

    public ItemStorageImpl(UserStorage userStorage) {
        id = 1;
        items = new HashMap<>();
        this.userStorage = userStorage;
    }

    private long generateId() {
        return id++;
    }

    @Override
    public List<Item> getItems() {
        return new ArrayList<Item>(items.values());
    }

    @Override
    public Optional<Item> getItemById(Long id) {
        if (items.containsKey(id)) {
            return Optional.of(items.get(id));
        }
        return Optional.empty();
    }

    @Override
    public Item addItem(Item item) {
        Long userId = Converter.stringToLong(item.getOwner());
        Optional<User> user = userStorage.getUserById(userId);
        if (user.isEmpty()) {
            throw new NotFoundException("Пользователь c id " + userId + " не найден");
        }
        long id = generateId();
        item.setId(id);
        items.put(id, item);

        return item;
    }

    @Override
    public Item updateItem(Item item) {
        return null;
    }

    @Override
    public void deleteItemById(long itemId) {

    }
}