package ru.practicum.shareit.item.comment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class CommentTest {
    private Comment commentOne;
    private Comment commentTwo;
    private User userAuthor;
    private Item item;

    @BeforeEach
    public void setUp() {
        item = new Item(1L, "ItemName", "DescriptionItem", true, userAuthor, 1L, 0);
        userAuthor = new User();
        userAuthor.setEmail("user@email.ru");
        userAuthor.setName("userName");
        userAuthor.setId(1L);
        commentOne = new Comment();
        commentOne.setId(1L);
        commentOne.setText("Text Comment");
        commentOne.setAuthor(userAuthor);
        commentOne.setItem(item);
        commentTwo = new Comment();
        commentTwo.setId(1L);
        commentTwo.setText("Text Comment");
        commentTwo.setAuthor(userAuthor);
        commentTwo.setItem(item);
    }

    @Test
    void testEqualsThisEqual() {
        Comment comment1 = commentOne;
        Comment comment2 = commentOne;

        assertTrue(comment2.equals(comment1));
    }

    @Test
    void testEqualsIsNull() {
        Comment comment1 = null;
        Comment comment2 = commentOne;

        assertFalse(comment2.equals(comment1));
    }

    @Test
    void testEqualsDifferentClass() {
        User user1 = userAuthor;
        Comment comment2 = commentOne;

        assertFalse(comment2.equals(user1));
    }

    @Test
    void testEqualsOneItem() {
        Comment comment1 = commentOne;
        Comment comment2 = (Comment) commentTwo;

        assertEquals(comment1.getId(), comment2.getId());
        assertTrue(comment2.equals(comment1));
    }

    @Test
    void testhashCode() {
        int hashOne = Objects.hash(commentOne.getId().hashCode());
        int hashTwo = Objects.hash(commentTwo.getId().hashCode());

        assertEquals(hashOne, hashTwo);
    }
}