package ru.practicum.shareitserver.item.comment.dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Builder
@Data
public class CommentDto {
    private Long id;
    private String text;
    private String authorName;
    @EqualsAndHashCode.Exclude
    private LocalDateTime created;
}