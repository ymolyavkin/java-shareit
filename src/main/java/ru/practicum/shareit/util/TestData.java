package ru.practicum.shareit.util;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.user.dto.IncomingUserDto;
import ru.practicum.shareit.user.model.User;

@UtilityClass
public class TestData {
    static Object[] argsProviderFactory() {
        return new Object[]{
                new Object[]{
                        1L,
                        new IncomingUserDto("Name", "email@email.ru"),
                        new User("Name", "email@email.ru"),
                        true
                },
                new Object[]{
                        1L,
                        new IncomingUserDto(null, null),
                        new User("Name", "email@email.ru"),
                        true
                },
                new Object[]{
                        1L,
                        new IncomingUserDto("Name", "email@email.ru"),
                        new User("NewName", "newemail@email.ru"),
                        true
                },
                new Object[]{
                        99L,
                        new IncomingUserDto("NewName", "newemail@email.ru"),
                        new User("NewName", "newemail@email.ru"),
                        false
                }

        };
    }
}
