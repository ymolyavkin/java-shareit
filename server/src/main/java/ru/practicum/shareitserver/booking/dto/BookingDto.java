package ru.practicum.shareitserver.booking.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareitserver.booking.model.Status;

import java.time.LocalDateTime;

@Builder
@Data
public class BookingDto {
    private long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Long itemId;
    private Long bookerId;
    private Status status;
}