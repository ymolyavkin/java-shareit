package ru.practicum.shareit.item.comment.dto;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareitserver.item.comment.dto.IncomingCommentDto;

import java.io.IOException;

@JsonTest
class IncomingCommentDtoTest {
    @Autowired
    private JacksonTester<IncomingCommentDto> json;

    @Test
    void incomingCommentDtoTest() throws IOException {
        IncomingCommentDto incomingCommentDto = new IncomingCommentDto();
        incomingCommentDto.setText("text");
        JsonContent<IncomingCommentDto> result = json.write(incomingCommentDto);

        Assertions.assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo("text");
    }
}