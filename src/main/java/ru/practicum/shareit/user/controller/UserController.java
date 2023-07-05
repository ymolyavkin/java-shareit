package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.IncomingUserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<User> getUsers() {
        log.info("Получен запрос на выдачу всех пользователей");
        return userService.getUsers();
    }

    @GetMapping("/{id}")
    public Optional<User> getUserById(@PathVariable Long id) {
        log.info("Получен запрос на выдачу пользователя по id = {}", id);
        return userService.getUserById(id);
    }

    @PostMapping(consumes = "application/json")
    public User addUser(@Valid @RequestBody IncomingUserDto incomingUserDto) {
        log.info("Получен запрос на добавление пользователя");
        return userService.addUser(incomingUserDto);
    }

    @PatchMapping(value = "/{id}", consumes = "application/json")
    public User updateUser(@RequestBody User user,
                           @PathVariable Long id) {
        log.info("Получен запрос на обновление пользователя с id = {}", id);
        return userService.updateUser(user, id);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteUserById(@PathVariable("id") long id) {
        log.info("Получен запрос на удаление пользователя с id = {}", id);
        userService.deleteUserById(id);
    }
}
