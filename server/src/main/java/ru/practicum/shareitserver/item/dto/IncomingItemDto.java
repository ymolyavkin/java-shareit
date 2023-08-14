package ru.practicum.shareitserver.item.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class IncomingItemDto {
    private String name;
    private String description;
    private Boolean available;
    private Long ownerId;
    private Long requestId;
}