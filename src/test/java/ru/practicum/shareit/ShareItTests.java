package ru.practicum.shareit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.comment.CommentRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.dto.IncomingUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
class ShareItTests {

    @Test
    void contextLoads() {
    }

    @BeforeEach
    void setUp() {
        User requestor = new User(1L, "Name Requestor", "email@yandex.ru");
       ItemRequest request = new ItemRequest("description", requestor, LocalDateTime.now());
        /*Item item = new Item();
       item.setId(1L);
        item.setName("Name");
        item.setDescription("descr");
        item.setAvailable(true);
        item.setOwnerId(1L);
        item.setRequestId(1);*/
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
        // Long itemRequestId = item.getRequestId().getId();
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