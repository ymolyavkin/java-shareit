package ru.practicum.shareit.item.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@RequiredArgsConstructor
@EqualsAndHashCode
public class IncomingItemDto {
    @Size(max = 255)
    @NotBlank(message = "Название вещи не может быть пустым.")
    private String name;
    @Size(max = 512)
    @NotBlank(message = "Описание вещи не может быть пустым.")
    private String description;
    @NotNull(message = "Доступность вещи для аренды должна быть указана.")
    private Boolean available;
    private Long ownerId;
    private Long requestId;
}