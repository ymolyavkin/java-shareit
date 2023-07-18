package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.booking.dto.BookingLastNextDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.dto.CommentMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

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
                .numberOfTimesToRent(item.getNumberOfTimesToRent())
                .build();
    }

    public static Item mapToItem(IncomingItemDto incomingItemDto, User owner) {
        Item item = new Item();
        item.setName(incomingItemDto.getName());
        item.setDescription(incomingItemDto.getDescription());
        item.setAvailable(incomingItemDto.getAvailable());
        item.setOwner(owner);

        return item;
    }

    /*public static ItemWithDateDto mapToItemWithDateDto(Item item, LocalDateTime start, LocalDateTime end) {
        return ItemWithDateDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .isAvailable(item.getAvailable())
                .ownerId(item.getOwnerId())
                .start(start)
                .end(end)
                .numberOfTimesToRent(item.getNumberOfTimesToRent())
                .build();
    }*/

    public static ItemLastNextDto mapToItemLastNextDto(Item item, Booking lastBooking, Booking nextBooking, List<Comment> comments) {
     /*   @AllArgsConstructor
        @Getter
        class NearestBookingDto implements BookingLastNextDto {
            private final Long id;
            private final Long bookerId;
        }*/
        /*Long lastBookingId = (lastBooking == null) ? null : lastBooking.getId();
        Long nextBookingId = (nextBooking == null) ? null : nextBooking.getId();
        new NearestBookingDto(lastBookingId, lastBookerId)
        Long lastBookerId = (lastBooking == null) ? null : lastBooking.getBookerId();
        Long nextBookerId = (nextBooking == null) ? null : nextBooking.getBookerId();*/
        // NearestBookingDto last = (lastBooking == null) ? null : new NearestBookingDto(lastBooking.getId(), lastBooking.getBookerId());
        BookingLastNextDto last = (lastBooking == null) ? null : new BookingLastNextDto(lastBooking.getId(), lastBooking.getBookerId());
        //NearestBookingDto next = (nextBooking == null) ? null : new NearestBookingDto(nextBooking.getId(), nextBooking.getBookerId());
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
                .comment(commentsOut)
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
                .comment(commentsOut)
                .build();
    }
}


