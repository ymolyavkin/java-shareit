package ru.practicum.shareit.request.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ItemRequestRepositoryTest {
    @Autowired
    private ItemRequestRepository itemRequestRepository;

    @Test
    void findAllByRequesterIdOrderByCreatedDesc() {
    }

    @Test
    void findAllByRequesterIdNotOrderByCreatedDesc() {
    }
}