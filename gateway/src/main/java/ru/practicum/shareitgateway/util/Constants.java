package ru.practicum.shareitgateway.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constants {
    public static final String USER_ID_FROM_REQUEST = "x-sharer-user-id";
    public static final String REQUEST_GIVE_ALL_USER_QUERIES = "Получен запрос на выдачу всех запросов пользователя с id = {}";
}
