package ru.practicum.shareit.util;

import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;

public class Constants {
    public static final String USER_ID_FROM_REQUEST = "x-sharer-user-id";
    public static final LocalDateTime DATE_TIME_NOW = LocalDateTime.now();
    public static final Sort SORT_BY_DESC = Sort.by(Sort.Direction.DESC, "start");
    public static final Sort SORT_BY_ID_ASC = Sort.by(Sort.Direction.ASC, "id");
}
