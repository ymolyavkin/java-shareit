package ru.practicum.shareitgateway.booking.dto;

import java.util.Optional;

public enum State {
    // Все
    ALL,
    // Текущие
    CURRENT,
    // Будущие
    FUTURE,
    // Ожидающие подтверждения
    WAITING;

    public static Optional<State> from(String stringState) {
        for (State state : values()) {
            if (state.name().equalsIgnoreCase(stringState)) {
                return Optional.of(state);
            }
        }
        return Optional.empty();
    }
}
