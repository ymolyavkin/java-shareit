package ru.practicum.shareitgateway.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareitgateway.client.BaseClient;
import ru.practicum.shareitgateway.request.dto.IncomingItemRequestDto;


import java.util.Map;

@Service
@Slf4j
public class ItemRequestClient extends BaseClient {
    private static final String API_PREFIX = "/requests";

    public ItemRequestClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> addItemRequest(long userId, IncomingItemRequestDto incomingItemRequestDto) {
        log.info("Добавить запрос на вещь от пользователя {}", userId);
        return post("", userId, incomingItemRequestDto);
    }

    public ResponseEntity<Object> getItemRequestById(Long requestId, long userId) {
        log.info("Получить данные о запросе с id = {}", requestId);
        return get("/" + requestId, userId);
    }

    public ResponseEntity<Object> getItemRequestsByOther(long userId, int from, int size) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size);
        log.info("Получить список запросов, зозданных другими пользователями", userId);
        return get("/all?from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> getItemRequestsByAuthor(long userId) {
        log.info("Получить список запросов пользователя с id =  {}", userId);
        return get("", userId);
    }

}