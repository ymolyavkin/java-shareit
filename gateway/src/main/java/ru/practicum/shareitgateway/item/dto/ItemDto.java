package ru.practicum.shareitgateway.item.dto;

import lombok.Builder;
import lombok.Data;

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