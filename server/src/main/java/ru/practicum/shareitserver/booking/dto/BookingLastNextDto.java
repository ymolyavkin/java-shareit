package ru.practicum.shareitserver.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BookingLastNextDto {
    private final long id;
    private final Long bookerId;
}

