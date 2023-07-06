package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.dto.IncomingUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    @Override
    public User addUser(IncomingUserDto incomingUserDto) {
        return userRepository.save(UserMapper.mapToUser(incomingUserDto));
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return UserMapper.mapToUserDto(users);
    }

    /*  @Override
      public User addUser(IncomingUserDto incomingUserDto) {
          return userStorage.addUser(ser);
      }
      User user = repository.save(UserMapper.mapToNewUser(userDto));
        return UserMapper.mapToUserDto(user);
      */
    @Transactional
    @Override
    public UserDto saveUser(IncomingUserDto userDto) {
        User user = userRepository.save(UserMapper.mapToUser(userDto));
        return UserMapper.mapToUserDto(user);
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
