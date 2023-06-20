package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Item;

public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
       /* return new ItemDto(
                item.getName(),
                item.getDescription(),
                item.isAvailable(),
                item.getRequest() != null ? item.getRequest().getId() : null
        );*/
        return new ItemDto.Builder()
                .id(0L)
                .name("Name")
                .description("descr")
                .isAvailable(true)
                .owner("owner")
                .request(request)
                .build();
    }

}
