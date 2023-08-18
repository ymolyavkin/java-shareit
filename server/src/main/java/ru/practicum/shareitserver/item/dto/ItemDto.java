package ru.practicum.shareitserver.item.dto;

import lombok.*;

@Builder
@Data
public class ItemDto {
    private long id;
    private String name;
    private String description;
    private boolean isAvailable;
    private long ownerId;
    private Long requestId;
    private int numberOfTimesToRent;
}