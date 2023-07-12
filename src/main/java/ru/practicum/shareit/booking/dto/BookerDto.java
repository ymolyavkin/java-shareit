package ru.practicum.shareit.booking.dto;

import lombok.Getter;
import lombok.ToString;

@Getter
public class BookerDto {
    private final long id;
    public BookerDto(long id) {
        this.id = id;
    }
}
