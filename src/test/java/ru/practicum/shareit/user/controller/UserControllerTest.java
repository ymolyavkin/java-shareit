package ru.practicum.shareit.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.IncomingUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserServiceImpl;

import javax.validation.ValidationException;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.jupiter.api.Assertions.*;

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
    private User user;
    private UserDto userDto;
    private IncomingUserDto incomingUserDto;

    @BeforeEach
    void setUp() {
        incomingUserDto = easyRandom.nextObject(IncomingUserDto.class);
        incomingUserDto.setEmail("newEmail@mail.ru");
        incomingUserDto.setName("newName@mail.ru");
        user = UserMapper.mapToUser(incomingUserDto);
        userDto = UserMapper.mapToUserDto(user);
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
    public void addUserWithEmptyNameValidationTest() throws Exception {
        incomingUserDto.setName("");
        when(userService.addUser(any())).thenThrow(new ValidationException("Name is not valid, name is empty"));
        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType("application/json")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createUserWithEmptyEmailValidationTest() throws Exception {
        incomingUserDto.setEmail("");
        when(userService.addUser(any())).thenThrow(new ValidationException("Email is not valid, email is empty"));
        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType("application/json")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createUserWithWrongEmailTest() throws Exception {
        incomingUserDto.setEmail("incorrect?.email.@");
        when(userService.addUser(any())).thenThrow(new ValidationException("Email isn't valid"));
        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType("application/json")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
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
    public void getUserByIdTest() throws Exception {
        when(userService.getUserById(anyLong())).thenReturn(userDto);
        mockMvc.perform(get("/users/{userId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userDto.getName())))
                .andExpect(jsonPath("$.email", is(userDto.getEmail())));
    }

    @Test
    public void getUserByWrongIdValidationTest() throws Exception {
        when(userService.getUserById(anyLong())).thenThrow(new NotFoundException("User has not been found"));
        mockMvc.perform(get("/users/{userId}", 999L))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getAllUsersTest() throws Exception {
        List<UserDto> users = new ArrayList<>();
        users.add(userDto);
        when(userService.getAllUsers()).thenReturn(users);
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(userDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(userDto.getName())))
                .andExpect(jsonPath("$[0].email", is(userDto.getEmail())));
    }

    @Test
    void deleteUserById() throws Exception {
        Long userId = 1L;
        mockMvc.perform(delete("/users/{id}", userId))
                .andDo(print())
                .andExpect(status().isOk());

        verify(userService).deleteUserById(userId);
    }

    @Test
    void updateUser_whenIncorrectIncomingData_thenBadRequest() throws Exception {
        Long userId = 1L;
        incomingUserDto.setEmail("mail.ru");
        incomingUserDto.setName("newName@mail.ru");
        User newUser = UserMapper.mapToUser(incomingUserDto);
        newUser.setId(userId);

        String result = mockMvc.perform(patch("/users/{id}", userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals("", result);
    }
}