package ru.practicum.shareit.booking.model;

import lombok.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Column(name = "start_time")
    private LocalDateTime start;
    @NotNull
    @Column(name = "end_time")
    private LocalDateTime end;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booker_id")
    private User booker;
    @Enumerated(EnumType.STRING)
    @JoinColumn(name = "status")
    private Status status = Status.WAITING;

    public Long getItemId() {
        return item.getId();
    }

    public Long getBookerId() {
        return booker.getId();
    }

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", start=" + start +
                ", end=" + end +
                ", item=" + item.getId() +
                ", booker=" + booker.getId() +
                ", status=" + status +
                '}';
    }
}