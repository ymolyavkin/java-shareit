package ru.practicum.shareit.item.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;
@Builder
@AllArgsConstructor
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Название вещи не может быть пустым.")
    private String name;
    @NotBlank(message = "Описание вещи не может быть пустым.")
    private String description;
    @NotNull(message = "Доступность вещи для аренды должна быть указана.")
    private Boolean available;
    private Long ownerId;
    private Integer requestId;
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
                ", request='" + requestId + '\'' +
                '}';
    }
}
