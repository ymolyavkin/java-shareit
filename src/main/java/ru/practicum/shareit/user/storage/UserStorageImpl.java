package ru.practicum.shareit.user.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.AlreadyExistsException;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Slf4j
@Repository
public class UserStorageImpl implements UserStorage {
    private long id;
    private Map<String, User> users;

    public UserStorageImpl() {
        id = 1;
        users = new HashMap<>();
    }

    private long generateId() {
        return id++;
    }
    @Override
    public User addUser(User user){
        if (users.containsKey(user.getEmail())) {
            throw new AlreadyExistsException("Пользователь с таким адресом электронной почты уже существует");
        } else {
            user.setId(generateId());
            users.put(user.getEmail(), user);
        }
        return user;
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<User>(users.values());
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return Optional.empty();
    }
}
