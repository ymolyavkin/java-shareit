package ru.practicum.shareit.item.dto;

import lombok.Getter;

@Getter
public class ItemWithIdAndNameDto {
    private final long id;
    private final String name;

    public ItemWithIdAndNameDto(long id, String name) {
        this.id = id;
        this.name = name;
    }
}
