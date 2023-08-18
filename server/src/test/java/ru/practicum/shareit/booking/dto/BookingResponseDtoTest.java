package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareitserver.booking.dto.BookerDto;
import ru.practicum.shareitserver.booking.dto.BookingResponseDto;
import ru.practicum.shareitserver.booking.model.Status;
import ru.practicum.shareitserver.item.dto.ItemIdNameDto;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;


@JsonTest
class BookingResponseDtoTest {
    @Autowired
    private JacksonTester<BookingResponseDto> jacksonTester;

    @Test
    public void dateTimeFormatTest() throws IOException {
        BookerDto bookerDto = new BookerDto() {
            private Long id = 1L;

            public Long getId() {
                return id;
            }
        };
        ItemIdNameDto itemIdNameDto = new ItemIdNameDto() {
            private Long id = 1L;
            private String name = "Item Name";

            public Long getId() {
                return id;
            }

            public String getName() {
                return name;
            }
        };
        BookingResponseDto bookingResponseDto = BookingResponseDto
                .builder()
                .id(1L).start(LocalDateTime.of(2023, 5, 9, 16, 0))
                .end(LocalDateTime.of(2023, 5, 10, 16, 0))
                .item(itemIdNameDto)
                .booker(bookerDto)
                .status(Status.WAITING).build();

        JsonContent<BookingResponseDto> result = jacksonTester.write(bookingResponseDto);

        assertThat(result).hasJsonPath("$.id");
        assertThat(result).hasJsonPath("$.start");
        assertThat(result).hasJsonPath("$.end");

        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo("2023-05-09T16:00:00");
    }
}