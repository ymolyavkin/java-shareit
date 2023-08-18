package ru.practicum.shareitserver.user.service;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareitserver.user.dto.IncomingUserDto;
import ru.practicum.shareitserver.user.dto.UserDto;
import ru.practicum.shareitserver.user.model.User;

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