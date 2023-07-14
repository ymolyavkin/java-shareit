package ru.practicum.shareit.item.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.comment.IncomingCommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    @Override
    public ItemDto addComment(IncomingCommentDto incomingCommentDto) {
        return null;
    }
}
