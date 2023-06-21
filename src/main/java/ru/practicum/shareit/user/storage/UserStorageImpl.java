package ru.practicum.shareit.user.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.AlreadyExistsException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Slf4j
@Repository
public class UserStorageImpl implements UserStorage {
    private long id;
    private Map<Long, User> users;

    private boolean emailAlreadyExists(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            return false;
        }
        for (User userValue : users.values()) {
            if (user.getEmail().equals(userValue.getEmail()) && user.getId() != userValue.getId()) {
                return true;
            }
        }
        return false;
    }

    public UserStorageImpl() {
        id = 1;
        users = new HashMap<>();
    }

    private long generateId() {
        return id++;
    }

    @Override
    public User addUser(User user) {
        if (emailAlreadyExists(user)) {
            throw new AlreadyExistsException("Пользователь с таким адресом электронной почты уже существует");
        } else {
            long id = generateId();
            user.setId(id);
            users.put(id, user);
        }
        return user;
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<User>(users.values());
    }

    @Override
    public Optional<User> getUserById(Long id) {
        if (users.containsKey(id)) {
            return Optional.of(users.get(id));
        }
        return Optional.empty();
    }

    @Override
    public User updateUser(User updatedUser, Long userId) {
        if (emailAlreadyExists(updatedUser)) {
            throw new AlreadyExistsException("Пользователь с таким адресом электронной почты уже существует");
        }

        if (users.containsKey(userId)) {
            User user = users.get(userId);
            if (updatedUser.getName() != null && !updatedUser.getName().isBlank() && !updatedUser.getName().equals(user.getName())) {
                user.setName(updatedUser.getName());
            }
            if (updatedUser.getEmail() != null && !updatedUser.getEmail().isBlank() && !updatedUser.getEmail().equals(user.getEmail())) {
                user.setEmail(updatedUser.getEmail());
            }
            updatedUser = user;
        } else {
            throw new NotFoundException("Пользователь не найден");
        }
        return updatedUser;
    }
}
