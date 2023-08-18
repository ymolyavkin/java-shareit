package ru.practicum.shareitserver.item.service;

import ru.practicum.shareitserver.item.comment.dto.CommentDto;
import ru.practicum.shareitserver.item.comment.dto.IncomingCommentDto;
import ru.practicum.shareitserver.item.dto.IncomingItemDto;
import ru.practicum.shareitserver.item.dto.ItemDto;
import ru.practicum.shareitserver.item.dto.ItemLastNextDto;

import java.util.List;

public interface ItemService {
    ItemLastNextDto getItemById(Long id, Long userId);

    List<ItemDto> searchItemsByText(String searchText, Integer from, Integer size);

    List<ItemLastNextDto> getItemsLastNextBookingByUser(Long userId, int from, int size);

    ItemDto addItem(IncomingItemDto incomingItemDto);

    ItemDto updateItem(IncomingItemDto incomingItemDto, Long itemId, Long userId);

    CommentDto addComment(IncomingCommentDto incomingCommentDto, Long userId, Long itemId);
}