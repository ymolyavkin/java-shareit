package ru.practicum.shareit.item.comment;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@RequiredArgsConstructor
public class IncomingCommentDto {
    @NotBlank(message = "Комментарий не может быть пустым.")
    private String text;
}
