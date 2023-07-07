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
                .ownerId(item.getOwner().getId())
                .numberOfTimesToRent(item.getNumberOfTimesToRent())
                .build();
    }

    public static Item toItem(IncomingItemDto incomingItemDto) {
        Item item = new Item();
       item.setName(incomingItemDto.getName());
       item.setDescription(incomingItemDto.getDescription());
      item.setAvailable(incomingItemDto.getAvailable());
      item.setOwnerId(incomingItemDto.getOwnerId());

      return item;
    }
}

