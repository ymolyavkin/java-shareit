package ru.practicum.shareit.item.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
@Component
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
