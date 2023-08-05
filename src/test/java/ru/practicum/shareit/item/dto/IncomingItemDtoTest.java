package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.json.JsonTest;

import java.io.IOException;
import java.time.LocalDateTime;

@JsonTest
class IncomingItemDtoTest {
    @Test
    void incomingItemDtoTest() throws IOException {
        LocalDateTime now = LocalDateTime.now();
    }
}