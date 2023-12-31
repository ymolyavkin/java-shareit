package ru.practicum.shareitserver.item.dto;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareitserver.booking.dto.BookingLastNextDto;
import ru.practicum.shareitserver.booking.model.Booking;
import ru.practicum.shareitserver.item.comment.Comment;
import ru.practicum.shareitserver.item.comment.dto.CommentDto;
import ru.practicum.shareitserver.item.comment.dto.CommentMapper;
import ru.practicum.shareitserver.item.model.Item;
import ru.practicum.shareitserver.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@UtilityClass
public class ItemMapper {

    public static ItemDto mapToItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .isAvailable(item.getAvailable())
                .ownerId(item.getOwnerId())
                .requestId(item.getRequestId())
                .numberOfTimesToRent(item.getNumberOfTimesToRent())
                .build();
    }

    public static Item mapToItem(IncomingItemDto incomingItemDto, User owner) {
        Item item = new Item();
        item.setName(incomingItemDto.getName());
        item.setDescription(incomingItemDto.getDescription());
        item.setAvailable(incomingItemDto.getAvailable());
        item.setOwner(owner);
        item.setRequestId(incomingItemDto.getRequestId());

        return item;
    }

    public static ItemLastNextDto mapToItemLastNextDto(Item item, Booking lastBooking, Booking nextBooking, List<Comment> comments) {
        BookingLastNextDto last = (lastBooking == null) ? null : new BookingLastNextDto(lastBooking.getId(), lastBooking.getBookerId());
        BookingLastNextDto next = (nextBooking == null) ? null : new BookingLastNextDto(nextBooking.getId(), nextBooking.getBookerId());
        List<CommentDto> commentsOut = comments
                .stream()
                .map(CommentMapper::mapToCommentDto)
                .collect(Collectors.toList());

        return ItemLastNextDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .isAvailable(item.getAvailable())
                .lastBooking(last)
                .nextBooking(next)
                .requestId(item.getRequestId())
                .comments(commentsOut)
                .build();
    }

    public static ItemLastNextDto mapToItemLastNextResponseDto(Item item, BookingLastNextDto last, BookingLastNextDto next, List<Comment> comments) {
        List<CommentDto> commentsOut = comments
                .stream()
                .map(CommentMapper::mapToCommentDto)
                .collect(Collectors.toList());

        return ItemLastNextDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .isAvailable(item.getAvailable())
                .lastBooking(last)
                .nextBooking(next)
                .comments(commentsOut)
                .build();
    }
}