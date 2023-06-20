package ru.practicum.shareit.booking;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Getter
@Setter
public class Booking {
    private long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Item item;
    private User booker;
    private Status status;

    public static class Builder {
        private final Booking newBooking;

        public Builder() {
            newBooking = new Booking();
        }

        public Booking.Builder id(long id) {
            newBooking.setId(id);
            return this;
        }
        public Booking.Builder start(LocalDateTime start) {
            newBooking.setStart(start);
            return this;
        }
        public Booking.Builder end(LocalDateTime end) {
            newBooking.setEnd(end);
            return this;
        }

        public Booking.Builder item(Item item) {
            newBooking.setItem(item);
            return this;
        }

        public Booking.Builder booker(User booker) {
            newBooking.setBooker(booker);
            return this;
        }

        public Booking.Builder status(Status status) {
            newBooking.setStatus(status);
            return this;
        }

        public Booking build() {
            return newBooking;
        }
    }
}
