package ru.practicum.shareitgateway.booking.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.practicum.shareitgateway.validator.Marker;
import ru.practicum.shareitgateway.validator.StartBeforeEndValidation;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@StartBeforeEndValidation
@RequiredArgsConstructor
public class IncomingBookingDto {
    @FutureOrPresent
    @NotNull(groups = Marker.OnCreate.class)
    private LocalDateTime start;
    @Future
    @NotNull(groups = Marker.OnCreate.class)
    private LocalDateTime end;
    @NotNull(groups = Marker.OnCreate.class)
    private Long itemId;
}
