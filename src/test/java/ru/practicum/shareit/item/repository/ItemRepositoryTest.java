package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ItemRepositoryTest {
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    UserRepository userRepository;

    @Test
    public void findItemIdsByOwnerIdTest() {
        User owner = new User(1L, "Name Qwner", "email@yandex.ru");
        userRepository.save(owner);
        Item itemOne = Item.builder()
                .id(1L)
                .name("Name")
                .description("descr")
                .available(true)
                .owner(owner)
                .build();
        Item itemTwo = Item.builder()
                .id(2L)
                .name("Name")
                .description("descr")
                .available(true)
                .owner(owner)
                .build();
        itemRepository.save(itemOne);
        itemRepository.save(itemTwo);
        List<Long> itemIds = itemRepository.findItemIdsByOwnerId(1L);

        assertEquals(2, itemIds.size());
    }
}