package ru.practicum.shareitgateway.booking.model;

import java.util.Optional;

public enum State {
    ALL,
    CURRENT,
    FUTURE,
    WAITING,
    REJECTED;
    public static Optional<State> fromString(String stringState) {
        for (State state : values()) {
            if (state.name().equalsIgnoreCase(stringState)) {
                return Optional.of(state);
            }
        }
        return Optional.empty();
    }
}

