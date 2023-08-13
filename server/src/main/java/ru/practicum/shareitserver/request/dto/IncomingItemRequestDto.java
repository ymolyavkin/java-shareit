package ru.practicum.shareitserver.request.dto;

import lombok.*;
import ru.practicum.shareitserver.validator.Marker;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class IncomingItemRequestDto {
    @Size(max = 512, groups = {Marker.OnCreate.class, Marker.OnUpdate.class})
    @NotBlank(groups = Marker.OnCreate.class)
    private String description;
}
