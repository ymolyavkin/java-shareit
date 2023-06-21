package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @GetMapping
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @PostMapping(consumes = "application/json")
    public User addUser(@Valid @RequestBody User user) {
        log.info("Получена сущность User");
        return userService.addUser(user);
    }
}
