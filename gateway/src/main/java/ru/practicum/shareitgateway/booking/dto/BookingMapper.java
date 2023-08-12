package ru.practicum.shareitgateway.booking.dto;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareitgateway.booking.model.Booking;
import ru.practicum.shareitgateway.item.dto.ItemIdNameDto;
import ru.practicum.shareitgateway.item.model.Item;
import ru.practicum.shareitgateway.user.model.User;


@Slf4j
@UtilityClass
public class BookingMapper {

    public static BookingResponseDto mapToBookingResponseDto(Booking booking, Item item) {
        ItemIdNameDto itemIdNameDto = new ItemIdNameDto() {
            private Long id = item.getId();
            private String name = item.getName();

            public Long getId() {
                return id;
            }

            public String getName() {
                return name;
            }
        };
        BookerDto bookerDto = new BookerDto() {
            private Long id = booking.getBookerId();

            public Long getId() {
                return id;
            }
        };

        return BookingResponseDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .item(itemIdNameDto)
                .booker(bookerDto)
                .status(booking.getStatus())
                .build();
    }

    public static BookingDto mapToBookingDto(Booking booking) {
        return BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .itemId(booking.getItemId())
                .bookerId(booking.getBookerId())
                .status(booking.getStatus())
                .build();
    }

    public static BookingLastNextDto mapToBookingLastNextDto(Booking booking) {
        return new BookingLastNextDto(booking.getId(), booking.getBookerId());
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



