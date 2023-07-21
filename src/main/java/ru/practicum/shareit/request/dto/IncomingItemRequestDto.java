package ru.practicum.shareit.request.dto;

import ru.practicum.shareit.validator.Marker;

import javax.validation.constraints.NotBlank;

public class IncomingItemRequestDto {
    @NotBlank(groups = Marker.OnCreate.class)
    private String description;
}
