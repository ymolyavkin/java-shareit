package ru.practicum.shareit.item.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByItem_Id(Long itemId);
<<<<<<< HEAD
}
=======
}
>>>>>>> 6cc5d081d5fc2f68fbe70910fb5eb6895ef10748
