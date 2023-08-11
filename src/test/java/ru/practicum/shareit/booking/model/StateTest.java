package ru.practicum.shareit.booking.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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