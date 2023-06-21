package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    @Override
    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    @Override
    public User addUser(User user) {
        return userStorage.addUser(user);
    }

    @Override
    public User updateUser(User user, Long userId) {
        return userStorage.updateUser(user, userId);
    }
    @Override
    public Optional<User> getUserById(Long id) {
        return userStorage.getUserById(id);
    }
    @Override
    public void deleteUserById(Long id) {
        userStorage.deleteUserById(id);
    }
}
