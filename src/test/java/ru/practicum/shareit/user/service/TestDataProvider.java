package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.IncomingUserDto;
import ru.practicum.shareit.user.model.User;

public class TestDataProvider {
    public static Object[] provideUpdateUser() {
        return// new Object[]{
                new Object[]{
                        new IncomingUserDto("Name", "email@email.ru"),
                        new User("Name", "email@email.ru"),
                        true
               // },
               /* new Object[]{
                        new IncomingUserDto(null, null),
                        new User("NewName", "newemail@email.ru"),
                        false*/
              //  }

        };
    }
    public static Object[] provideUpdateUserData() {
        return new Object[]{
                new Object[]{
                        new IncomingUserDto("Name", "email@email.ru"),
                        new User("Name", "email@email.ru"),
                        true
                },
                new Object[]{
                        new IncomingUserDto(null, null),
                        new User("NewName", "newemail@email.ru"),
                        false
                }

        };
    }
}
