package ru.practicum.shareit.item.comment.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@EqualsAndHashCode
@RequiredArgsConstructor
public class IncomingCommentDto {
    @NotBlank(message = "Комментарий не может быть пустым.")
    private String text;
}