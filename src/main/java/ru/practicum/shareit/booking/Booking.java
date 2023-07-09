package ru.practicum.shareit.booking;

import lombok.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;

@Builder
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name="bookings")
public class Booking {
    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private LocalDateTime start;
    @NotNull
    private LocalDateTime end;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="item_id")
    private Item item;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="booker_id")
    private User booker;
    @Enumerated(EnumType.STRING)
    @JoinColumn(name="status")
    private Status status;

//    public static class Builder {
//        private final Booking newBooking;
//
//        public Builder() {
//            newBooking = new Booking();
//        }
//
//        public Booking.Builder id(long id) {
//            newBooking.setId(id);
//            return this;
//        }
//
//        public Booking.Builder start(LocalDateTime start) {
//            newBooking.setStart(start);
//            return this;
//        }
//
//        public Booking.Builder end(LocalDateTime end) {
//            newBooking.setEnd(end);
//            return this;
//        }
//
//        public Booking.Builder item(Item item) {
//            newBooking.setItem(item);
//            return this;
//        }
//
//        public Booking.Builder booker(User booker) {
//            newBooking.setBooker(booker);
//            return this;
//        }
//
//        public Booking.Builder status(Status status) {
//            newBooking.setStatus(status);
//            return this;
//        }
//
//        public Booking build() {
//            return newBooking;
//        }
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Booking booking = (Booking) o;
        return id == booking.id && start.equals(booking.start) && end.equals(booking.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, start, end);
    }

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", start=" + start +
                ", end=" + end +
                ", item=" + item +
                ", booker=" + booker +
                ", status=" + status +
                '}';
    }
}
