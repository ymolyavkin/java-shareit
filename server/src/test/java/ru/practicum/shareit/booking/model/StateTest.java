package ru.practicum.shareit.booking.model;

import org.junit.jupiter.api.Test;
import ru.practicum.shareitserver.booking.model.State;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class StateTest {

    @Test
    void values() {
        assertEquals(5, State.values().length);
    }

    @Test
    void valueOf() {
        assertAll(
                () -> assertEquals(State.valueOf("ALL"), State.ALL),
                () -> assertEquals(State.valueOf("CURRENT"), State.CURRENT),
                () -> assertEquals(State.valueOf("FUTURE"), State.FUTURE),
                () -> assertEquals(State.valueOf("WAITING"), State.WAITING),
                () -> assertEquals(State.valueOf("REJECTED"), State.REJECTED)
        );
    }
}