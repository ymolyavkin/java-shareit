package ru.practicum.shareitserver.item.comment.dto;

import lombok.*;
import ru.practicum.shareitserver.validator.Marker;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IncomingCommentDto {
    @Size(max = 512, groups = {Marker.OnCreate.class, Marker.OnUpdate.class})
    @NotBlank(groups = {Marker.OnCreate.class}, message = "Комментарий не может быть пустым.")
    private String text;
}