package ru.practicum.shareitserver.item.comment.dto;

import lombok.*;

@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IncomingCommentDto {
    private String text;
}