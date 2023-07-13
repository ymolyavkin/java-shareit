package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemIdName;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

@Slf4j
@UtilityClass
public class BookingMapper {

    public static BookingResponseDto mapToBookingResponseDto(Booking booking, Item item) {
        @AllArgsConstructor
        @Getter
        class ItemWithIdAndNameDto implements ItemIdName {
            private final long id;
            private final String name;
        }
        return BookingResponseDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .item(new ItemWithIdAndNameDto(item.getId(), item.getName()))
                .booker(new BookerDto(booking.getBookerId()))
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
}

