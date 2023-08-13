package ru.practicum.shareitserver.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareitserver.exception.NotFoundException;
import ru.practicum.shareitserver.user.dto.IncomingUserDto;
import ru.practicum.shareitserver.user.dto.UserDto;
import ru.practicum.shareitserver.user.dto.UserMapper;
import ru.practicum.shareitserver.user.model.User;
import ru.practicum.shareitserver.user.repository.UserRepository;

import java.util.List;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

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

    @Transactional
    @Override
    public UserDto updateUser(IncomingUserDto incomingUserDto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с id %d не найден", userId)));

        if (incomingUserDto.getName() != null && !incomingUserDto.getName().isBlank()) {
            user.setName(incomingUserDto.getName());
        }
        if (incomingUserDto.getEmail() != null && !incomingUserDto.getEmail().isBlank()) {
            user.setEmail(incomingUserDto.getEmail());
        }

        userRepository.saveAndFlush(user);
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
    }
}