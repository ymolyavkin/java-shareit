package ru.practicum.shareit.request.dto;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class ItemRequestResponseDto {
    private Long id;
    private String description;
    private LocalDateTime created;

    public ItemRequestResponseDto(Long id, String description, LocalDateTime created) {
        this.id=id;
        this.description=description;
        this.created=created;
    }
}
