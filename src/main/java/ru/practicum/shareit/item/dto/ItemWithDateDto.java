package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class ItemWithDateDto {
    private long id;
    private String name;
    private String description;
    private boolean isAvailable;
    private long ownerId;
    private LocalDateTime start;
    private LocalDateTime end;
    private int numberOfTimesToRent;
}