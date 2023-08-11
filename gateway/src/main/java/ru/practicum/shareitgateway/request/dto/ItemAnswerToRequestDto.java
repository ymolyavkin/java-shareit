package ru.practicum.shareitgateway.request.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ItemAnswerToRequestDto {
    private long id;
    private String name;
    private String description;
    private Long requestId;
    private boolean available;
}