package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.dto.IncomingUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    /*@Override
    public List<User> getUsers() {
        return userStorage.getUsers();
    }*/

    @Override
    public User addUser(IncomingUserDto incomingUserDto) {
        return userRepository.save(UserMapper.mapToUser(incomingUserDto));
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return UserMapper.mapToUserDto(users);
    }

    @Override
    public UserDto saveUser(IncomingUserDto userDto) {
        User user = userRepository.save(UserMapper.mapToUser(userDto));
        return UserMapper.mapToUserDto(user);
    }

    @Override
    public UserDto updateUser(IncomingUserDto incomingUserDto, Long userId) {
        // User user = userRepository.getReferenceById(userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с id %d не найден", userId)));

        boolean needsToBeChanged = false;
        if (incomingUserDto.getName() != null && !incomingUserDto.getName().equals(user.getName())) {
            user.setName(incomingUserDto.getName());
            needsToBeChanged = true;
        }
        if (incomingUserDto.getEmail() != null && !incomingUserDto.getEmail().equals(user.getEmail())) {
            user.setEmail(incomingUserDto.getEmail());
            needsToBeChanged = true;
        }
        if (needsToBeChanged) {
            userRepository.saveAndFlush(user);
        }
        return UserMapper.mapToUserDto(user);
    }

    @Override
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с id %d не найден", id)));
        return UserMapper.mapToUserDto(user);
    }

    @Override
    public void deleteUserById(Long id) {
        userRepository.findById(id).ifPresent(user -> userRepository.deleteById(id));

        /*Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            userRepository.deleteById(id);
        } else throw new NotFoundException(String.format("Пользователь с id %d не найден", id));*/

    }
}
