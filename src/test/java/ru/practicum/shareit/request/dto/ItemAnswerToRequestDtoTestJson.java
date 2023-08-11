package ru.practicum.shareit.request.dto;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.io.IOException;

@JsonTest
class ItemAnswerToRequestDtoTestJson {
    @Autowired
    private JacksonTester<ItemAnswerToRequestDto> json;

    @Test
    void itemAnswerToRequestDtoTest() throws IOException {
        User user = new User();
        user.setEmail("email@email.ru");
        user.setName("Name");

        Item item = new Item();
        item.setId(1L);
        item.setName("ItemName");
        item.setDescription("ItemDescription");
        item.setAvailable(true);
        item.setOwner(user);
        item.setRequestId(1L);

        ItemAnswerToRequestDto itemAnswerToRequestDto = ItemAnswerToRequestDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .requestId(item.getRequestId())
                .build();

        JsonContent<ItemAnswerToRequestDto> result = json.write(itemAnswerToRequestDto);

        Assertions.assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        Assertions.assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(itemAnswerToRequestDto.getDescription());
    }
}