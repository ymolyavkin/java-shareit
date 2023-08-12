package ru.practicum.shareitserver.booking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.practicum.shareitserver.booking.model.Status;
import ru.practicum.shareit.item.dto.ItemIdNameDto;

import java.time.LocalDateTime;

@Builder
@Data
public class BookingResponseDto {
    private long id;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime start;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime end;
    @EqualsAndHashCode.Exclude
    private ItemIdNameDto item;
    @EqualsAndHashCode.Exclude
    private BookerDto booker;
    private Status status;
}

