package ru.practicum.shareit.item.model;

import lombok.*;
import ru.practicum.shareit.user.model.User;

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
    //  private Long ownerId;
    @ManyToOne(fetch = FetchType.LAZY)
    // исключаем все поля с отложенной загрузкой из метода toString,
    // чтобы не было случайных обращений к базе данных, например при выводе в лог.
    //  @ToString.Exclude
    @JoinColumn(name = "owner_id")
    private User owner;
    @ToString.Exclude
    private Integer requestId;
    @Column(name = "number_of_times_to_rent")
    private int numberOfTimesToRent;

    public void incrementNumberOfTimesToRent() {
        numberOfTimesToRent++;
    }

    public Long getOwnerId() {
        return owner.getId();
    }

    public void setOwnerId(Long id) {
        owner.setId(id);
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
                ", owner='" + owner.getId() + '\'' +
                ", request='" + requestId + '\'' +
                '}';
    }
}
