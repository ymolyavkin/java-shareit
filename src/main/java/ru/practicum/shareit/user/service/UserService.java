package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getUsers();

    User addUser(User user);
    User updateUser(User user, Long userId);

    Optional<User> getUserById(Long id);

    void deleteUserById(Long id);
}
