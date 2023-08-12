package ru.practicum.shareit.util;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;
import static ru.practicum.shareitgateway.util.Time.isOverlapping;

class TimeTest {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private final LocalDateTime time1 = LocalDateTime.parse("2023-01-14 11:04", formatter);
    private final LocalDateTime time2 = LocalDateTime.parse("2023-02-14 11:04", formatter);
    private final LocalDateTime time3 = LocalDateTime.parse("2023-03-14 11:04", formatter);
    private final LocalDateTime time4 = LocalDateTime.parse("2023-04-14 11:04", formatter);
    private LocalDateTime start1;
    private LocalDateTime end1;
    private LocalDateTime start2;
    private LocalDateTime end2;

    @Test
    void isOverlapping1423() {
        start1 = time1;
        end1 = time4;
        start2 = time2;
        end2 = time3;
        boolean result = isOverlapping(start1, end1, start2, end2);
        assertTrue(result);
    }

    @Test
    void isOverlapping14232() {
        start2 = time1;
        end2 = time4;
        start1 = time2;
        end1 = time3;
        boolean result = isOverlapping(start1, end1, start2, end2);
        assertTrue(result);
    }

    @Test
    void isOverlapping2413() {
        start1 = time2;
        end1 = time4;
        start2 = time1;
        end2 = time3;
        boolean result = isOverlapping(start1, end1, start2, end2);
        assertTrue(result);
    }

    @Test
    void isOverlapping1324() {
        start1 = time1;
        end1 = time3;
        start2 = time2;
        end2 = time4;
        boolean result = isOverlapping(start1, end1, start2, end2);
        assertTrue(result);
    }

    @Test
    void isOverlapping1234() {
        start2 = time1;
        end2 = time2;
        start1 = time3;
        end1 = time4;
        boolean result = isOverlapping(start1, end1, start2, end2);
        assertFalse(result);
    }
}