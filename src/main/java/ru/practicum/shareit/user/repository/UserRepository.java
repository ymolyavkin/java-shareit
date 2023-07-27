package ru.practicum.shareit.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByEmailContainingIgnoreCase(String emailSearch);
<<<<<<< HEAD
}
=======
}
>>>>>>> 6cc5d081d5fc2f68fbe70910fb5eb6895ef10748
