package ru.practicum.shareitserver.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingLastNextDto;
import ru.practicum.shareitserver.item.comment.dto.CommentDto;

import java.util.List;

@Builder
@Data
public class ItemLastNextDto {
    private Long id;
    private String name;
    private String description;
    private boolean isAvailable;
    private BookingLastNextDto lastBooking;
    private BookingLastNextDto nextBooking;
    private Long requestId;
    private List<CommentDto> comments;
}