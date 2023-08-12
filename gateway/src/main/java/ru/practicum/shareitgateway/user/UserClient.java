package ru.practicum.shareitgateway.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareitgateway.client.BaseClient;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import ru.practicum.shareitgateway.user.dto.IncomingUserDto;

@Service
@Slf4j
public class UserClient extends BaseClient {
    private static final String API_PREFIX = "/users";

    @Autowired
    public UserClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build()
        );
    }

    public ResponseEntity<Object> addUser(IncomingUserDto incomingUserDto) {
        log.info("Добавление пользователя");
        return post("", incomingUserDto);
    }

    public ResponseEntity<Object> getUserById(Long userId) {
        log.info("Получить пользователя с id = {}", userId);
        return get("/" + userId);
    }

    public ResponseEntity<Object> updateUserById(Long userId, IncomingUserDto incomingUserDto) {
        log.info("Обновить пользователя с id = {}", userId);
        return patch("/" + userId, incomingUserDto);
    }

    public ResponseEntity<Object> getAllUsers() {
        log.info("Получить список всех пользователей");
        return get("");
    }

    public ResponseEntity<Object> deleteUserById(Long userId) {
        log.info("Удалить пользователя с id = {}", userId);
        return delete("/" + userId);
    }
}