package ru.practicum.shareit.item.dto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Item;
@Service
@Slf4j
@RequiredArgsConstructor
public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
       /* return new ItemDto(
                item.getName(),
                item.getDescription(),
                item.isAvailable(),
                item.getRequest() != null ? item.getRequest().getId() : null
        );*/
        Long itemRequestId = item.getRequest().getId();
        return new ItemDto.Builder()
                .id(0L)
                .name("Name")
                .description("descr")
                .isAvailable(true)
                .owner("owner")
                .request(itemRequestId)
                .build();
    }
}
