package ru.practicum.shareit.item.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

/**
 * TODO Sprint add-controllers.
 */
@Getter
@Setter
public class Item {
    private long id;
    @NotBlank(message = "Название вещи не может быть пустым.")
    private String name;
    private String description;
    private boolean available;
    private String owner;
    private String request;

    public static class Builder {
        private final Item newItem;

        public Builder() {
            newItem = new Item();
        }

        public Builder id(Long id) {
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

        public Builder available(boolean available) {
            newItem.setAvailable(available);
            return this;
        }

        public Builder owner(String owner) {
            newItem.setOwner(owner);
            return this;
        }

        public Builder request(String request) {
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
}
