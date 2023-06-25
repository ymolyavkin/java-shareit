package ru.practicum.shareit.item.dto;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.item.model.Item;

@Slf4j
@UtilityClass
public class ItemMapper {

    public static ItemDto toItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .isAvailable(item.getAvailable())
                .ownerId(item.getOwnerId())
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

