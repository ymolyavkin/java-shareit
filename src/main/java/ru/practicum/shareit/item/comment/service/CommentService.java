package ru.practicum.shareit.item.comment.service;

import ru.practicum.shareit.item.comment.IncomingCommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

public interface CommentService {
    ItemDto addComment(IncomingCommentDto incomingCommentDto);
}
