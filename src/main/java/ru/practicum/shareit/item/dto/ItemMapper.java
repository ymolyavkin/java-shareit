package ru.practicum.shareit.item.dto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemMapper {
    private final UserStorage userStorage;

    public ItemDto toItemDto(Item item) {
        Long userId = item.getOwnerId();
        Optional<User> owner = userStorage.getUserById(userId);
        if (owner.isPresent()) {
            String ownerName = owner.get().getName();
            return new ItemDto.Builder()
                    .id(item.getId())
                    .name(item.getName())
                    .description(item.getDescription())
                    .isAvailable(item.getAvailable())
                    .owner(ownerName)
                    .numberOfTimesToRent(item.getNumberOfTimesToRent())
                    .build();
        } else throw new NotFoundException("Пользователь c id " + userId + " не найден");
    }
}
