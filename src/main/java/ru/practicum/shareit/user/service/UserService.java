package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.IncomingUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Transactional(readOnly=true)
public interface UserService {
   /* @Transactional(readOnly=true)
    List<User> getUsers();*/
    @Transactional
    User addUser(IncomingUserDto incomingUserDto);
    List<UserDto> getAllUsers();
    /*  @Override
      public User addUser(IncomingUserDto incomingUserDto) {
          return userStorage.addUser(ser);
      }*/
      @Transactional
      UserDto saveUser(IncomingUserDto userDto);

  /*  @Transactional
    User updateUser(User user, Long userId);*/

    /*@Override
    public User updateUser(User user, Long userId) {
        return userStorage.updateUser(user, userId);
    }*/
    UserDto updateUser(IncomingUserDto incomingUserDto, Long userId);

    @Transactional(readOnly=true)
    UserDto getUserById(Long id);
    @Transactional
    void deleteUserById(Long id);
}
