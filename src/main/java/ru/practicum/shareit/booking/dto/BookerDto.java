package ru.practicum.shareit.booking.dto;

import lombok.Getter;

@Getter
public class BookerDto {
    private final long id;

    public BookerDto(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "BookerDto{" +
                "id=" + id +
                '}';
    }
}
