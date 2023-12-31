package ru.practicum.shareitserver.util;

import lombok.experimental.UtilityClass;
import ru.practicum.shareitserver.booking.model.StateRequest;
import ru.practicum.shareitserver.user.dto.IncomingUserDto;
import ru.practicum.shareitserver.user.model.User;

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

    static Object[] argsProviderFactoryBookingsByOwner() {
        return new Object[]{
                new Object[]{
                        3L,
                        StateRequest.ALL
                },
                new Object[]{
                        99L,
                        StateRequest.ALL
                },
                new Object[]{
                        3L,
                        StateRequest.CURRENT
                },
                new Object[]{
                        3L,
                        StateRequest.PAST
                },
                new Object[]{
                        3L,
                        StateRequest.FUTURE
                },
                new Object[]{
                        3L,
                        StateRequest.WAITING
                },
                new Object[]{
                        3L,
                        StateRequest.REJECTED
                },
                new Object[]{
                        3L,
                        StateRequest.UNSUPPORTED_STATUS
                }
        };
    }
}
