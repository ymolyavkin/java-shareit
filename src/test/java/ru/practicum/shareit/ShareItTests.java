package ru.practicum.shareit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@SpringBootTest
class ShareItTests {

    @Test
    void contextLoads() {
    }

    @BeforeEach
    void setUp() {
        User requestor = new User(1L, "Name Requestor", "email@yandex.ru");
        ItemRequest request = new ItemRequest("description", requestor, LocalDateTime.now());

        Item item = Item.builder()
                .id(1L)
                .name("Name")
                .description("descr")
                .available(true)
                .owner(requestor)
                .build();
        User booker = new User("Name", "email@mail.ru");

        Booking booking = Booking.builder()
                .id(0L)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now())
                .item(item)
                .booker(booker)
                .status(Status.WAITING)
                .build();

        ItemDto itemDto = ItemDto.builder()
                .id(0L)
                .name("Name")
                .description("descr")
                .isAvailable(true)
                .ownerId(0L)
                .build();
    }


    @AfterEach
    void tearDown() {
    }

}