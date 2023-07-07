package ru.practicum.shareit.item.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class ItemStorageImpl implements ItemStorage {
    private AtomicLong id;
    private final Map<Long, Item> items;
    private final UserStorage userStorage;

    public ItemStorageImpl(UserStorage userStorage) {
        id = new AtomicLong(1);
        items = new HashMap<>();
        this.userStorage = userStorage;
    }

    private long generateId() {
        return id.getAndIncrement();
    }

    @Override
    public List<Item> getItems() {
        return new ArrayList<>(items.values());
    }

    @Override
    public List<Item> getItems(Long userId) {
       // return items.values().stream().filter(item -> item.getOwnerId().equals(userId)).collect(Collectors.toList());
        return null;
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
        Long userId = item.getOwner().getId();
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
    public Item updateItem(Item updatedItem, Long itemId, Long userId) {
        Optional<User> user = userStorage.getUserById(userId);
        if (user.isEmpty()) {
            throw new NotFoundException("Пользователь c id " + userId + " не найден");
        }
        if (items.containsKey(itemId)) {
            Item item = items.get(itemId);
            if (!item.getOwnerId().equals(userId)) {
                throw new NotFoundException("Редактировать вещь может только её владелец");
            }
            if (updatedItem.getName() != null && !updatedItem.getName().isBlank() && !updatedItem.getName().equals(item.getName())) {
                item.setName(updatedItem.getName());
            }
            if (updatedItem.getDescription() != null &&
                    !updatedItem.getDescription().isBlank() &&
                    !updatedItem.getDescription().equals(item.getDescription())) {
                item.setDescription(updatedItem.getDescription());
            }
            if (updatedItem.getAvailable() != null && updatedItem.getAvailable() != item.getAvailable()) {
                if (item.getAvailable() && !updatedItem.getAvailable()) {
                    item.incrementNumberOfTimesToRent();
                }
                item.setAvailable(updatedItem.getAvailable());
            }
            updatedItem = item;
        } else {
            throw new NotFoundException("Вещь не найдена");
        }
        return updatedItem;
    }

    @Override
    public Item getItemById(long itemId) {
        if (items.containsKey(itemId)) {
            return items.get(itemId);
        } else {
            throw new NotFoundException("Вещь c id " + id + " не найдена");
        }
    }

    @Override
    public List<Item> searchItems(String keyword) {
        return items.values()
                .stream()
                .filter(item -> item.getAvailable()
                        && (item.getName().toLowerCase().contains(keyword.toLowerCase())
                        || item.getDescription().toLowerCase().contains(keyword.toLowerCase())))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteItemById(long itemId) {
        if (items.containsKey(itemId)) {
            items.remove(itemId);
            log.info("Вещь с id = {} удалена.", itemId);
        } else {
            log.info("Вещь с id = {} не существует.", itemId);
        }
    }
}
