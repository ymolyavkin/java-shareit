package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.Status;

import java.time.LocalDateTime;
import java.util.Map;

@Builder
@Data
public class BookingWithItemMapDto {
    private long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Map<String, String> item;
    private Long bookerId;
    private Status status;
}
