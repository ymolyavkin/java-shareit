package ru.practicum.shareit.item.comment;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class CommentRepositoryTest {
    @Autowired
    private CommentRepository commentRepository;

    @Test
    void findByItem_Id() {
    }
}