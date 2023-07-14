package ru.practicum.shareit.item.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.dto.BookingLastNextDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.IncomingCommentDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

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


//    public static CommentWithDateDto mapToCommentWithDateDto(Comment comment, LocalDateTime start, LocalDateTime end) {
//        return CommentWithDateDto.builder()
//                .id(comment.getId())
//                .name(comment.getName())
//                .description(comment.getDescription())
//                .isAvailable(comment.getAvailable())
//                .ownerId(comment.getOwnerId())
//                .start(start)
//                .end(end)
//                .numberOfTimesToRent(comment.getNumberOfTimesToRent())
//                .build();
//    }
//
//    public static CommentLastNextDto mapToCommentLastNextDto(Comment comment, Booking lastBooking, Booking nextBooking) {
//        @AllArgsConstructor
//        @Getter
//        class NearestBookingDto implements BookingLastNextDto {
//            private final Long id;
//            private final Long bookerId;
//        }
//        Long lastBookingId = (lastBooking == null) ? null : lastBooking.getId();
//        Long nextBookingId = (nextBooking == null) ? null : nextBooking.getId();
//
//        Long lastBookerId = (lastBooking == null) ? null : lastBooking.getBookerId();
//        Long nextBookerId = (nextBooking == null) ? null : nextBooking.getBookerId();
//
//        return CommentLastNextDto.builder()
//                .id(comment.getId())
//                .name(comment.getName())
//                .description(comment.getDescription())
//                .isAvailable(comment.getAvailable())
//                .lastBooking(new NearestBookingDto(lastBookingId, lastBookerId))
//                .nextBooking(new NearestBookingDto(nextBookingId, nextBookerId))
//                .build();
//    }
}
