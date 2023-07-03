package ru.practicum.shareit.item.model;

import lombok.*;
import ru.practicum.shareit.request.ItemRequest;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Data
@Builder
public class Item {
    private long id;
    @NotBlank(message = "Название вещи не может быть пустым.")
    private String name;
    @NotBlank(message = "Описание вещи не может быть пустым.")
    private String description;
    @NotNull(message = "Доступность вещи для аренды должна быть указана.")
    private Boolean available;
    private Long ownerId;
    private ItemRequest request;
    private int numberOfTimesToRent;

    public void incrementNumberOfTimesToRent() {
        numberOfTimesToRent++;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return id == item.id && name.equals(item.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", available=" + available +
                ", owner='" + ownerId + '\'' +
                ", request='" + request + '\'' +
                '}';
    }
}
