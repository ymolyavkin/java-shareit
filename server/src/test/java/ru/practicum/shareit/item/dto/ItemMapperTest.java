package ru.practicum.shareit.item.dto;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import ru.practicum.shareitserver.item.dto.ItemDto;
import ru.practicum.shareitserver.item.dto.ItemMapper;
import ru.practicum.shareitserver.item.model.Item;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ItemMapperTest {
    private final EasyRandom generator = new EasyRandom();

    @Test
    void mapToItemDto() {
        Item item = generator.nextObject(Item.class);
        ItemDto itemDto = ItemMapper.mapToItemDto(item);

        assertEquals(item.getId(), itemDto.getId());
        assertEquals(item.getName(), itemDto.getName());
        assertEquals(item.getDescription(), itemDto.getDescription());
        assertEquals(item.getAvailable(), itemDto.isAvailable());
        assertEquals(item.getOwnerId(), itemDto.getOwnerId());
    }
}