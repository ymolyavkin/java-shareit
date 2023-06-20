package ru.practicum.shareit.item;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
public class ItemController {
    public static void main(String[] args) {
        Item item = new Item.Builder()
                .id(0L)
                .name("Name")
                .description("descr")
                .available(true)
                .owner("owner")
                .request("request")
                .build();
        User booker = new User("Name", "email@mail.ru");

        Booking booking = new Booking.Builder()
                .id(0L)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now())
                .item(item)
                .booker(booker)
                .status(Status.WAITING)
                .build();

        ItemRequest itemRequest = new ItemRequest("description", booker, LocalDateTime.now());

        System.out.println();
    }
}
