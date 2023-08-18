package ru.practicum.shareitserver.item.comment.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareitserver.item.comment.Comment;
import ru.practicum.shareitserver.item.model.Item;
import ru.practicum.shareitserver.user.model.User;

import java.time.LocalDateTime;

@UtilityClass
public class CommentMapper {
    public static CommentDto mapToCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorName(comment.getAuthorName())
                .created(comment.getCreated())
                .build();
    }

    public static Comment mapToComment(IncomingCommentDto incomingCommentDto, User author, Item item) {
        Comment comment = new Comment();
        comment.setText(incomingCommentDto.getText());
        comment.setAuthor(author);
        comment.setItem(item);
        comment.setCreated(LocalDateTime.now());
        return comment;
    }
}