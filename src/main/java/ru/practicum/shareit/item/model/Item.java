package ru.practicum.shareit.item.model;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.request.ItemRequest;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Getter
@Setter
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

    public static class Builder {
        private final Item newItem;

        public Builder() {
            newItem = new Item();
        }

        public Builder id(long id) {
            newItem.setId(id);
            return this;
        }

        public Builder name(String name) {
            newItem.setName(name);
            return this;
        }

        public Builder description(String description) {
            newItem.setDescription(description);
            return this;
        }

        public Builder isAvailable(Boolean isAavailable) {
            newItem.setAvailable(isAavailable);
            return this;
        }

        public Builder ownerId(Long ownerId) {
            newItem.setOwnerId(ownerId);
            return this;
        }

        public Builder request(ItemRequest request) {
            newItem.setRequest(request);
            return this;
        }

        public Item build() {
            return newItem;
        }
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
