package ru.practicum.shareit.item.comment.dto;

import lombok.Builder;
import lombok.Data;


import java.util.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Builder
@Data
public class CommentDto {
    private Long id;
    private String text;
    private String authorName;
    private LocalDateTime created;
}
