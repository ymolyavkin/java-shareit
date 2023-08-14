package ru.practicum.shareitgateway.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareitgateway.user.dto.IncomingUserDto;
import ru.practicum.shareitgateway.validator.Marker;

@Controller
@RequestMapping(value = "/users")
@Slf4j
@Validated
@RequiredArgsConstructor
public class UserController {
    private final UserClient userClient;

    @GetMapping
    public ResponseEntity<Object> getUsers() {
        log.info("Получен запрос на выдачу всех пользователей");

        return userClient.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable Long id) {
        log.info("Получен запрос на выдачу пользователя по id = {}", id);

        return userClient.getUserById(id);
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<Object> addUser(@Validated({Marker.OnCreate.class})
                                          @RequestBody IncomingUserDto incomingUserDto) {
        log.info("Получен запрос на добавление пользователя");

        return userClient.addUser(incomingUserDto);
    }

    @PatchMapping(value = "/{id}", consumes = "application/json")
    public ResponseEntity<Object> updateUser(@Validated({Marker.OnUpdate.class})
                                             @RequestBody IncomingUserDto incomingUserDto,
                                             @PathVariable Long id) {
        log.info("Получен запрос на обновление пользователя с id = {}", id);

        return userClient.updateUserById(id, incomingUserDto);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> deleteUserById(@PathVariable("id") long id) {
        log.info("Получен запрос на удаление пользователя с id = {}", id);

        return userClient.deleteUserById(id);
    }
}