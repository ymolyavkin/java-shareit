package ru.practicum.shareitserver.item.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.practicum.shareitserver.validator.Marker;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@RequiredArgsConstructor
@EqualsAndHashCode
public class IncomingItemDto {
    @Size(max = 255, groups = {Marker.OnCreate.class, Marker.OnUpdate.class})
    @NotBlank(groups = {Marker.OnCreate.class}, message = "Название вещи не может быть пустым.")
    private String name;
    @Size(max = 512, groups = {Marker.OnCreate.class, Marker.OnUpdate.class})
    @NotBlank(groups = {Marker.OnCreate.class}, message = "Описание вещи не может быть пустым.")
    private String description;
    @NotNull(groups = {Marker.OnCreate.class}, message = "Доступность вещи для аренды должна быть указана.")
    private Boolean available;
    private Long ownerId;
    private Long requestId;
}