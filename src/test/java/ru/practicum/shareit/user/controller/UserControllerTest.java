package ru.practicum.shareit.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.IncomingUserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserServiceImpl;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc
class UserControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserServiceImpl userService;
    private final EasyRandom easyRandom = new EasyRandom();

    @Test
    void getUsers() {
    }

    @Test
    void getUserById() throws Exception {
        Long userId = 1L;
        mockMvc.perform(get("/users/{id}", userId))
                .andDo(print())
                .andExpect(status().isOk());
        verify(userService).getUserById(userId);
    }

    @Test
    void addUser() throws Exception {
        Long userId = 1L;
        IncomingUserDto incomingUserDto = easyRandom.nextObject(IncomingUserDto.class);
        incomingUserDto.setEmail("newEmail@mail.ru");
        incomingUserDto.setName("newName@mail.ru");
        User user = UserMapper.mapToUser(incomingUserDto);

        when(userService.addUser(incomingUserDto)).thenReturn(user);

        String result = mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals(objectMapper.writeValueAsString(user), result);
    }

    @Test
    void updateUser() throws Exception {
        Long userId = 1L;
        IncomingUserDto incomingUserDto = easyRandom.nextObject(IncomingUserDto.class);
        User oldUser = UserMapper.mapToUser(incomingUserDto);
        oldUser.setId(userId);

        incomingUserDto.setEmail("newEmail@mail.ru");
        incomingUserDto.setName("newName@mail.ru");
        User newUser = UserMapper.mapToUser(incomingUserDto);
        newUser.setId(userId);

        when(userService.updateUser(incomingUserDto, userId)).thenReturn(UserMapper.mapToUserDto(newUser));

        String result = mockMvc.perform(patch("/users/{id}", userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals(objectMapper.writeValueAsString(newUser), result);
    }

    @Test
    void deleteUserById() {
    }
}