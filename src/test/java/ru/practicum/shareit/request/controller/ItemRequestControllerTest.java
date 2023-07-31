package ru.practicum.shareit.request.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import ru.practicum.shareit.user.controller.UserController;

import static org.junit.jupiter.api.Assertions.*;
@WebMvcTest(controllers = ItemRequestController.class)
@AutoConfigureMockMvc
class ItemRequestControllerTest {

    @Test
    void addItemRequest() {
    }

    @Test
    void getItemRequestsByAuthor() {
    }

    @Test
    void getItemRequestsByOther() {
    }

    @Test
    void getItemRequestById() {
    }
}