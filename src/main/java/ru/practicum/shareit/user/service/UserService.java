package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.IncomingUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface UserService {
    @Transactional
    User addUser(IncomingUserDto incomingUserDto);

    List<UserDto> getAllUsers();

    @Transactional
    UserDto saveUser(IncomingUserDto userDto);

    UserDto updateUser(IncomingUserDto incomingUserDto, Long userId);

    @Transactional(readOnly = true)
    UserDto getUserById(Long id);

    @Transactional
    void deleteUserById(Long id);
}