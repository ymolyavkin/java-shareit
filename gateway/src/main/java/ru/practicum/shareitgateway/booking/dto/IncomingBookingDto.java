package ru.practicum.shareitgateway.booking.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.practicum.shareitgateway.booking.model.Status;
import ru.practicum.shareitgateway.validator.Marker;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
public class IncomingBookingDto {
    @NotNull(groups = Marker.OnCreate.class)
    private LocalDateTime start;
    @NotNull(groups = Marker.OnCreate.class)
    private LocalDateTime end;
    private Long itemId;
    private Status status = Status.WAITING;
}
