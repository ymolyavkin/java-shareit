package ru.practicum.shareitgateway.request.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode
public class ItemRequestResponseDto {
    private Long id;
    private String description;
    private LocalDateTime created;

    public ItemRequestResponseDto(Long id, String description, LocalDateTime created) {
        this.id = id;
        this.description = description;
        this.created = created;
    }
}