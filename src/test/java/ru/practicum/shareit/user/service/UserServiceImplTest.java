package ru.practicum.shareit.user.service;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.IncomingUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceImpl userService;
    private final EasyRandom easyRandom = new EasyRandom();

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void addUser_whenUserValid_thenSavedUser() {
        IncomingUserDto incomingUserDto = easyRandom.nextObject(IncomingUserDto.class);
        User userToSave = UserMapper.mapToUser(incomingUserDto);

        when(userRepository.save(userToSave)).thenReturn(userToSave);

        User addedUser = userService.addUser(incomingUserDto);

        assertEquals(addedUser, userToSave);
    }

    @Test
    void getAllUsers() {
    }

    @Test
    void saveUser() {
        IncomingUserDto incomingUserDto = easyRandom.nextObject(IncomingUserDto.class);
        User userToSave = UserMapper.mapToUser(incomingUserDto);
        when(userRepository.save(userToSave)).thenReturn(userToSave);

        UserDto actualUser = userService.saveUser(incomingUserDto);

        assertEquals(UserMapper.mapToUserDto(userToSave), actualUser);
        verify(userRepository).save(userToSave);
    }

    @Test
    void updateUser() {
    }

    @Test
    void getUserById_WhenUserFound_thenReturnedUser() {
        long userId = 1L;
        User expectUser = new User();
        expectUser.setId(userId);

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(expectUser));
        UserDto userDto = userService.getUserById(userId);

        assertEquals(userDto, UserMapper.mapToUserDto(expectUser));
    }

    @Test
    void getUserById_WhenUserNotFound_thenExceptionThrown() {
        long userId = 1L;
        when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.getUserById(userId));
    }

    @Test
    void deleteUserById() {
    }
}