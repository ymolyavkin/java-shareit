package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.ItemIdNameDto;

import java.time.LocalDateTime;

@Builder
@Data
public class BookingResponseDto {
    private long id;
    @JsonFormat(pattern="dd.MM.yyyy HH:mm")
    private LocalDateTime start;
    @JsonFormat(pattern="dd.MM.yyyy HH:mm")
    private LocalDateTime end;
    private ItemIdNameDto item;
    private BookerDto booker;
    private Status status;
}