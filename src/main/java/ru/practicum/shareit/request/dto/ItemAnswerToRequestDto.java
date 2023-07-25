package ru.practicum.shareit.request.dto;

import lombok.Builder;

@Builder
public class ItemAnswerToRequestDto {
    private long itemId;
    private String name;
    private String description;
    private long requestId;
    private boolean available;
}
