package ru.practicum.shareit.item.dto;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Status;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
@JsonTest
class IncomingItemDtoTest {
    @Test
    void incomingItemDtoTest() throws IOException {
        LocalDateTime now = LocalDateTime.now();
//        IncomingItemDtoTest bookingDto = BookingDto.builder()
//                .id(1L)
//                .status(Status.WAITING)
//                .start(now.plusDays(1))
//                .end(now.plusDays(2))
//                .build();
//        JsonContent<BookingDto> result = json.write(bookingDto);
//
//        Assertions.assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
//        Assertions.assertThat(result).extractingJsonPathStringValue("$.status").isEqualTo("WAITING");
//        Assertions.assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo((now.plusDays(1)).format(formatter));
//        Assertions.assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo((now.plusDays(2)).format(formatter));
    }
}