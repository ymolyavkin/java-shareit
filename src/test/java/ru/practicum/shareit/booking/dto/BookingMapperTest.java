package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.ItemIdNameDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class BookingMapperTest {
    private Booking booking;
    private User user;
    private Item item;
    private User booker;
    private Booking lastBooking;
    private Booking nextBooking;
    private IncomingBookingDto incomingBookingDto;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private final LocalDateTime dateTime = LocalDateTime.parse("2023-01-14 11:04", formatter);

    private final LocalDateTime start = dateTime.plusDays(1);
    private final LocalDateTime end = dateTime.plusDays(2);

    @BeforeEach
    void beforeEach() {
        booker = new User();
        booker.setEmail("user@email.ru");
        booker.setName("userName");
        booker.setId(1L);
        item = new Item(1L, "ItemName", "DescriptionItem", true, booker, 1L, 0);
        lastBooking = new Booking(1L, start, end, item, booker, Status.WAITING);
        nextBooking = new Booking(2L, dateTime.plusDays(2), dateTime.plusDays(3), item, booker, Status.WAITING);
        incomingBookingDto = new IncomingBookingDto();
        incomingBookingDto.setBookerId(booker.getId());
        incomingBookingDto.setItemId(item.getId());

        incomingBookingDto.setStart(dateTime.plus(1, ChronoUnit.DAYS));
        incomingBookingDto.setEnd(dateTime.plus(2, ChronoUnit.DAYS));


        booking = new Booking(3L, start, end, item, booker, Status.WAITING);
    }

    @Test
    void mapToBookingResponseDto() {
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

        BookingResponseDto bookingResponseDto = BookingResponseDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .item(itemIdNameDto)
                .booker(bookerDto)
                .status(booking.getStatus())
                .build();

        assertThat(booking.getId()).isEqualTo(3L);
        assertThat(booking.getStart()).isEqualTo(start);
        assertThat(booking.getEnd()).isEqualTo(end);
    }

    @Test
    void mapToBookingDto() {
        BookingDto bookingDto = BookingDto.builder()
                .id(lastBooking.getId())
                .start(lastBooking.getStart())
                .end(lastBooking.getEnd())
                .itemId(item.getId())
                .bookerId(lastBooking.getBookerId())
                .status(lastBooking.getStatus())
                .build();

        assertThat(bookingDto.getId()).isEqualTo(1L);
        assertThat(bookingDto.getStart()).isEqualTo(start);
        assertThat(bookingDto.getEnd()).isEqualTo(end);
        assertThat(bookingDto.getItemId()).isEqualTo(item.getId());
        assertThat(bookingDto.getBookerId()).isEqualTo(1L);
        assertThat(bookingDto.getStatus()).isEqualTo(Status.WAITING);
    }

    @Test
    void mapToBookingLastNextDto() {
        BookingLastNextDto bookingLastNextDto = new BookingLastNextDto(booking.getId(), booking.getBookerId());

        assertThat(bookingLastNextDto.getId()).isEqualTo(booking.getId());
        assertThat(bookingLastNextDto.getBookerId()).isEqualTo(booking.getBookerId());
    }

    @Test
    void mapToBooking() {
        Booking booking = new Booking();
        booking.setStart(incomingBookingDto.getStart());
        booking.setEnd(incomingBookingDto.getEnd());
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStatus(incomingBookingDto.getStatus());

        assertThat(incomingBookingDto.getStart()).isEqualTo(booking.getStart());
        assertThat(incomingBookingDto.getEnd()).isEqualTo(booking.getEnd());
        assertThat(incomingBookingDto.getStatus()).isEqualTo(booking.getStatus());
        assertThat(incomingBookingDto.getItemId()).isEqualTo(item.getId());
        assertThat(incomingBookingDto.getBookerId()).isEqualTo(booker.getId());
    }
}