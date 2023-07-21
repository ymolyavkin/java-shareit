package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.dto.IncomingCommentDto;
import ru.practicum.shareit.item.dto.IncomingItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemLastNextDto;

import java.util.List;

public interface ItemService {
    ItemLastNextDto getItemById(Long id, Long userId);

    List<ItemDto> searchItemsByText(String searchText);

    List<ItemLastNextDto> getItemsLastNextBookingByUser(Long userId);

    List<ItemDto> getItemsByUser(Long userId);

    ItemDto addItem(IncomingItemDto incomingItemDto);

    ItemDto updateItem(IncomingItemDto incomingItemDto, Long itemId, Long userId);

    CommentDto addComment(IncomingCommentDto incomingCommentDto, Long userId, Long itemId);
}