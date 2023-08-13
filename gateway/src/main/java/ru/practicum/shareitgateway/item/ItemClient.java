package ru.practicum.shareitgateway.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareitgateway.client.BaseClient;
import ru.practicum.shareitgateway.item.comment.dto.IncomingCommentDto;
import ru.practicum.shareitgateway.item.dto.IncomingItemDto;

import java.util.Map;
@Slf4j
@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> getItemsByUser(long userId, int from, int size) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size);
        return get("?from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> getItemById(long itemId, long userId) {
        log.info("Получен запрос на выдачу вещи с id = {} пользователем с id = {}", itemId, userId);
        return get("/" + itemId, userId);
    }

    public ResponseEntity<Object> addItem(long userId, IncomingItemDto incomingItemDto) {
        return post("", userId, incomingItemDto);
    }

    public ResponseEntity<Object> updateItem(IncomingItemDto incomingItemDto, long itemId, long userId) {
        return patch("/" + itemId, userId, incomingItemDto);
    }

    public ResponseEntity<Object> searchItemsByText(long userId, String text, int from, int size) {
        Map<String, Object> parameters = Map.of(
                "text", text,
                "from", from,
                "size", size);
        return get("/search?text={text}&from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> addComment(long userId, long itemId, IncomingCommentDto incomingCommentDto) {
        return post("/" + itemId + "/comment", userId, incomingCommentDto);
    }
}