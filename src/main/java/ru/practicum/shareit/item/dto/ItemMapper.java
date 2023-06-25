package ru.practicum.shareit.item.dto;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.util.Converter;

@Slf4j
@UtilityClass
public class ItemMapper {

    public static ItemDto toItemDto(Item item) {
        return new ItemDto.Builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .isAvailable(item.getAvailable())
                .owner(item.getOwnerId())
                .numberOfTimesToRent(item.getNumberOfTimesToRent())
                .build();
    }

    public static Item toItem(IncomingItemDto incomingItemDto) {
        return new Item.Builder()
                .name(incomingItemDto.getName())
                .description(incomingItemDto.getDescription())
                .isAvailable(incomingItemDto.getAvailable())
                .ownerId(incomingItemDto.getOwnerId())
                .build();
    }
}

