package ru.practicum.shareitgateway.booking.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareitgateway.booking.model.Status;

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