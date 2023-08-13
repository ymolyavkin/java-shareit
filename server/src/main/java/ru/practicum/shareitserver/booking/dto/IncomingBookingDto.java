package ru.practicum.shareitserver.booking.dto;

import lombok.*;
import ru.practicum.shareitserver.booking.model.Status;
import ru.practicum.shareitserver.validator.Marker;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IncomingBookingDto {
    @NotNull(groups = Marker.OnCreate.class)
    private LocalDateTime start;
    @NotNull(groups = Marker.OnCreate.class)
    private LocalDateTime end;
    private Long itemId;
    private Long bookerId;
    private Status status = Status.WAITING;
}