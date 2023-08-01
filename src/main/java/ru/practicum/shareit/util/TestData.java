package ru.practicum.shareit.util;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.model.StateRequest;
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

    static Object[] argsProviderFactoryBookingsByBooker() {
        return new Object[]{
                new Object[]{
                        1L,
                        StateRequest.ALL
                },
                new Object[]{
                        99L,
                        StateRequest.ALL
                },
                new Object[]{
                        1L,
                        StateRequest.CURRENT
                },
                new Object[]{
                        1L,
                        StateRequest.PAST
                },
                new Object[]{
                        1L,
                        StateRequest.FUTURE
                },
                new Object[]{
                        1L,
                        StateRequest.WAITING
                },
                new Object[]{
                        1L,
                        StateRequest.REJECTED
                },
                new Object[]{
                        1L,
                        StateRequest.UNSUPPORTED_STATUS
                }
        };
    }
}
