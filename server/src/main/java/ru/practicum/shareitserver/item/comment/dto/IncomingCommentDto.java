package ru.practicum.shareitserver.item.comment.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.validator.Marker;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@EqualsAndHashCode
@RequiredArgsConstructor
public class IncomingCommentDto {
    @Size(max = 512, groups = {Marker.OnCreate.class, Marker.OnUpdate.class})
    @NotBlank(groups = {Marker.OnCreate.class}, message = "Комментарий не может быть пустым.")
    private String text;
}