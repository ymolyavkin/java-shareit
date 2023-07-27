package ru.practicum.shareit.util;

import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
@UtilityClass
public class Time {
    public static boolean isOverlapping(LocalDateTime start1, LocalDateTime end1, LocalDateTime start2, LocalDateTime end2) {
        return start1.isBefore(end2) && start2.isBefore(end1);
    }
}