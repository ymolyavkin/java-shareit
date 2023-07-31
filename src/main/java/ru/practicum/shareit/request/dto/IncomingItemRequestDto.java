package ru.practicum.shareit.request.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.validator.Marker;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@RequiredArgsConstructor
@EqualsAndHashCode
public class IncomingItemRequestDto {
    @NotBlank(groups = Marker.OnCreate.class)
    private String description;
}
