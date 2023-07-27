package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingLastNextDto;
import ru.practicum.shareit.item.comment.dto.CommentDto;

import java.util.List;

@Data
public class ItemResponseDto {
    private long id;
    private String name;
    private String description;
    private boolean isAvailable;
    private BookingLastNextDto lastBooking;
    private BookingLastNextDto nextBooking;
    private List<CommentDto> comment;
}
