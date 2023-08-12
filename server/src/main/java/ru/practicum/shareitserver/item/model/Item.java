package ru.practicum.shareitserver.item.model;

import lombok.*;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
    @Size(max = 255)
    @NotBlank(message = "Название вещи не может быть пустым.")
    @Column(name = "name")
    private String name;
    @Size(max = 512)
    @NotBlank(message = "Описание вещи не может быть пустым.")
    @Column(name = "description")
    private String description;
    @NotNull(message = "Доступность вещи для аренды должна быть указана.")
    @Column(name = "available")
    private Boolean available;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id")
    private User owner;
    @ToString.Exclude
    @Column(name = "request_id")
    private Long requestId;
    @Column(name = "number_of_times_to_rent")
    private int numberOfTimesToRent;

    public void incrementNumberOfTimesToRent() {
        numberOfTimesToRent++;
    }

    public Long getOwnerId() {
        return owner.getId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return id.equals(item.id) && name.equals(item.name);
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
                ", owner='" + this.getOwnerId() + '\'' +
                '}';
    }
}