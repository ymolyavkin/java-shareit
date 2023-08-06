package ru.practicum.shareit.item.comment.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@EqualsAndHashCode
@RequiredArgsConstructor
public class IncomingCommentDto {
    @Size(max = 512)
    @NotBlank(message = "Комментарий не может быть пустым.")
    private String text;
}