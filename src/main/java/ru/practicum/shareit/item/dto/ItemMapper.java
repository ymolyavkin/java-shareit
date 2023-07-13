package ru.practicum.shareit.item.dto;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Slf4j
@UtilityClass
public class ItemMapper {

    public static ItemDto mapToItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .isAvailable(item.getAvailable())
                .ownerId(item.getOwnerId())
                .numberOfTimesToRent(item.getNumberOfTimesToRent())
                .build();
    }

    public static Item mapToItem(IncomingItemDto incomingItemDto, User owner) {
        Item item = new Item();
        item.setName(incomingItemDto.getName());
        item.setDescription(incomingItemDto.getDescription());
        item.setAvailable(incomingItemDto.getAvailable());
        item.setOwner(owner);

        return item;
    }
    public static ItemWithDateDto mapToItemWithDateDto(Item item, LocalDateTime start, LocalDateTime end) {
        return ItemWithDateDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .isAvailable(item.getAvailable())
                .ownerId(item.getOwnerId())
                .start(start)
                .end(end)
                .numberOfTimesToRent(item.getNumberOfTimesToRent())
                .build();
    }
}

