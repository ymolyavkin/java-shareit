package ru.practicum.shareit.request.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class ItemRequestDto {
    private Long id;
    @NotBlank
    private String description;
    private Long requestorId;
    private LocalDateTime created;
}
