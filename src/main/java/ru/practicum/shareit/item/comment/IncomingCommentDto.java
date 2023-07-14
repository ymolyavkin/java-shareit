package ru.practicum.shareit.item.comment;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class IncomingCommentDto {
    private String text;
}
