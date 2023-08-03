package ru.practicum.shareit.user.service;

import junitparams.JUnitParamsRunner;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.IncomingUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(JUnitParamsRunner.class)
@ExtendWith(MockitoExtension.class)
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceImpl userService;
    private final EasyRandom easyRandom = new EasyRandom();

    @Test
    void addUser_whenUserValid_thenSavedUser() {
        IncomingUserDto incomingUserDto = easyRandom.nextObject(IncomingUserDto.class);
        User userToSave = UserMapper.mapToUser(incomingUserDto);

        when(userRepository.save(userToSave)).thenReturn(userToSave);

        UserDto actualUser = userService.saveUser(incomingUserDto);

        assertEquals(UserMapper.mapToUserDto(userToSave), actualUser);
        verify(userRepository).save(userToSave);
    }

    @Test
    void addUser() {
        IncomingUserDto incomingUserDto = easyRandom.nextObject(IncomingUserDto.class);
        User userToSave = UserMapper.mapToUser(incomingUserDto);

        when(userService.addUser(incomingUserDto)).thenReturn(userToSave);

        UserDto actualUser = userService.saveUser(incomingUserDto);

        assertEquals(UserMapper.mapToUserDto(userToSave), actualUser);
        verify(userRepository).save(userToSave);
    }

    @Test
    void getAllUsers() {
        List<UserDto> expectedUsers = Collections.emptyList();

        when(userService.getAllUsers()).thenReturn(Collections.emptyList());
        System.out.println();

        List<UserDto> actualUsers = userService.getAllUsers();

        assertEquals(expectedUsers.size(), actualUsers.size());
    }

    @Test
    void saveUser() {
        IncomingUserDto incomingUserDto = easyRandom.nextObject(IncomingUserDto.class);
        User userToSave = UserMapper.mapToUser(incomingUserDto);
        when(userRepository.save(userToSave)).thenReturn(userToSave);

        UserDto actualUser = userService.saveUser(incomingUserDto);

        assertEquals(UserMapper.mapToUserDto(userToSave), actualUser);
        verify(userRepository).save(userToSave);
        verify(userRepository, times(1)).save(userToSave);
    }

    @ParameterizedTest
    @MethodSource("ru.practicum.shareit.util.TestData#argsProviderFactory")
    void updateUserWithParameters(Long userId, IncomingUserDto incomingUserDto, User user, boolean needsToBeChanged) {
        if (userId.equals(99L)) {
            when(userRepository.findById(userId))
                    .thenReturn(Optional.empty());

            assertThrows(NotFoundException.class, () -> userService.updateUser(incomingUserDto, userId));
        }
        if (incomingUserDto.getName() == null || incomingUserDto.getEmail() == null) {
            when(userRepository.findById(userId))
                    .thenReturn(Optional.of(user));
            userService.updateUser(incomingUserDto, userId);

            verify(userRepository, times(0)).save(user);
        } else {
            when(userRepository.findById(userId)).thenReturn(Optional.of(user));

            UserDto actualUserDto = userService.updateUser(incomingUserDto, userId);
            User newUser = UserMapper.mapToUser(incomingUserDto);

            assertEquals(actualUserDto.getEmail(), newUser.getEmail());
            assertEquals(actualUserDto.getName(), newUser.getName());
            assertEquals(actualUserDto.getId(), newUser.getId());
        }
    }

    @Test
    void updateUser() {
        Long userId = 1L;
        IncomingUserDto incomingUserDto = easyRandom.nextObject(IncomingUserDto.class);
        User oldUser = UserMapper.mapToUser(incomingUserDto);
        oldUser.setId(userId);

        incomingUserDto.setEmail("newEmail@mail.ru");
        incomingUserDto.setName("newName@mail.ru");
        User newUser = UserMapper.mapToUser(incomingUserDto);
        newUser.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(oldUser));

        UserDto actualUserDto = userService.updateUser(incomingUserDto, userId);

        verify(userRepository).saveAndFlush(newUser);
        assertEquals(actualUserDto.getEmail(), newUser.getEmail());
        assertEquals(actualUserDto.getName(), newUser.getName());
        assertEquals(actualUserDto.getId(), newUser.getId());
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
    void deleteUserById_WhenUserNotFound_thenExceptionThrown() {
        long userId = 1L;
        when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.getUserById(userId));
    }

    @Test
    void deleteUserById_WhenUserFound_thenDelete() {
        long userId = 1L;
        User expectUser = new User();
        expectUser.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(expectUser));

        userService.deleteUserById(userId);

        verify(userRepository, times(1)).deleteById(userId);
    }
}