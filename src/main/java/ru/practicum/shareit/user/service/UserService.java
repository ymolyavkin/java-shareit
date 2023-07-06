package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.IncomingUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
@Transactional(readOnly=true)
public interface UserService {
    @Transactional(readOnly=true)
    List<User> getUsers();
    @Transactional
    User addUser(IncomingUserDto incomingUserDto);
    List<UserDto> getAllUsers();
    /*  @Override
      public User addUser(IncomingUserDto incomingUserDto) {
          return userStorage.addUser(ser);
      }*/
      @Transactional
      UserDto saveUser(IncomingUserDto userDto);

    @Transactional
    User updateUser(User user, Long userId);
    @Transactional(readOnly=true)
    Optional<User> getUserById(Long id);
    @Transactional
    void deleteUserById(Long id);
}
