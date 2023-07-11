package ru.practicum.shareit.booking.dto;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.item.dto.ItemWithIdAndNameDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.Map;

@Slf4j
@UtilityClass
public class BookingMapper {

    public static BookingWithItemIdAndNameDto mapToBookingWithItemIdAndNameDto(Booking booking, ItemWithIdAndNameDto itemWithIdAndNameDto) {
        BookingWithItemIdAndNameDto bookWee = BookingWithItemIdAndNameDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .item(itemWithIdAndNameDto)
                .bookerId(booking.getBookerId())
                .status(booking.getStatus())
                .build();
        System.out.println();
        return BookingWithItemIdAndNameDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .item(itemWithIdAndNameDto)
                .bookerId(booking.getBookerId())
                .status(booking.getStatus())
                .build();
    }
    public static BookingDto mapToBookingDto(Booking booking) {
        return BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .bookerId(booking.getBookerId())
                .status(booking.getStatus())
                .build();
    }

    public static Booking mapToBooking(IncomingBookingDto incomingBookingDto, Item item, User booker) {
        Booking booking = new Booking();
        booking.setStart(incomingBookingDto.getStart());
        booking.setEnd(incomingBookingDto.getEnd());
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStatus(incomingBookingDto.getStatus());

        return booking;
    }
    public static BookingWithItemMapDto mapToBookingWithItemMapDto(Booking booking, Map<String, String> item) {
        return BookingWithItemMapDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .bookerId(booking.getBookerId())
                .item(item)
                .status(booking.getStatus())
                .build();
    }
}

