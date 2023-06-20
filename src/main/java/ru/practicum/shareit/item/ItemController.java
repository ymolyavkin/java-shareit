package ru.practicum.shareit.item;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.item.model.Item;

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
        System.out.println();
    }
}
