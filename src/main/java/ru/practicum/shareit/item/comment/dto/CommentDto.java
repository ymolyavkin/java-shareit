package ru.practicum.shareit.item.comment.dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Builder
@Data
public class CommentDto {
    private Long id;
    @Size(max = 512)
    private String text;
    private String authorName;
    @EqualsAndHashCode.Exclude
    private LocalDateTime created;
}