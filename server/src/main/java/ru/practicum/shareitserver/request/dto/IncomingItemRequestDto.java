package ru.practicum.shareitserver.request.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class IncomingItemRequestDto {
    private String description;
}
