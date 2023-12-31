package ru.practicum.shareit.booking.dto;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.test.context.junit4.SpringRunner;
import ru.practicum.shareitserver.booking.dto.BookingDto;
import ru.practicum.shareitserver.booking.model.Status;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RunWith(SpringRunner.class)
@JsonTest
class BookingDtoTest {
    @Autowired
    private JacksonTester<BookingDto> json;
    private static final String TIME_PATTERN_TEST = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSS";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TIME_PATTERN_TEST);

    @Test
    void bookingCreationDtoTest() throws IOException {
        LocalDateTime now = LocalDateTime.parse("2019-02-14T11:04:52.78423108");
        BookingDto bookingDto = BookingDto.builder()
                .id(1L)
                .status(Status.WAITING)
                .start(now.plusDays(1))
                .end(now.plusDays(2))
                .build();
        JsonContent<BookingDto> result = json.write(bookingDto);

        Assertions.assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        Assertions.assertThat(result).extractingJsonPathStringValue("$.status").isEqualTo("WAITING");
        Assertions.assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo((now.plusDays(1)).format(formatter));
        Assertions.assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo((now.plusDays(2)).format(formatter));
    }
}