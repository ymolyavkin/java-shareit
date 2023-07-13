package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.ItemIdName;

import java.time.LocalDateTime;

@Builder
@Data
public class BookingWithItemIdAndNameDto {
    private long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private ItemIdName item;
    private BookerDto booker;
    private Status status;
}
