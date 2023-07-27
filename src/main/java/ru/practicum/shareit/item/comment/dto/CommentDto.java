package ru.practicum.shareit.item.comment.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class CommentDto {
    private Long id;
    private String text;
    private String authorName;
    private LocalDateTime created;
<<<<<<< HEAD
}
=======
}
>>>>>>> 6cc5d081d5fc2f68fbe70910fb5eb6895ef10748
