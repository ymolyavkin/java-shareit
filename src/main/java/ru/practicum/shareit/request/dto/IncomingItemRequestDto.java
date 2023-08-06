package ru.practicum.shareit.request.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.validator.Marker;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@RequiredArgsConstructor
@EqualsAndHashCode
public class IncomingItemRequestDto {
    @Size(max = 512)
    @NotBlank(groups = Marker.OnCreate.class)
    private String description;
}
