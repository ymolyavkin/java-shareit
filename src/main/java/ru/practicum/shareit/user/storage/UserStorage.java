package ru.practicum.shareit.user.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    List<User> getUsers();

    Optional<User> getUserById(Long id);

    User addUser(User user);

    User updateUser(User user, Long userId);

    void deleteUserById(long userId);
}
