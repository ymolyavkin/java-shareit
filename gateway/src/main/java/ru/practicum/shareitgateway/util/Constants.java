package ru.practicum.shareitgateway.util;

import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;

@UtilityClass
public class Constants {
    public static final String USER_ID_FROM_REQUEST = "x-sharer-user-id";
    public static final LocalDateTime DATE_TIME_NOW = LocalDateTime.now();
    public static final String REQUEST_GIVE_ALL_USER_QUERIES = "Получен запрос на выдачу всех запросов пользователя с id = {}";
    public static final Sort SORT_BY_START_DESC = Sort.by(Sort.Direction.DESC, "start");
    public static final Sort SORT_BY_START_ASC = Sort.by(Sort.Direction.ASC, "start");
    public static final Sort SORT_BY_CREATED_DESC = Sort.by(Sort.Direction.DESC, "created");
    public static final Sort SORT_BY_ID_ASC = Sort.by(Sort.Direction.ASC, "id");
}
