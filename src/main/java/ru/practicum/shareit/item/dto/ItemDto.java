package ru.practicum.shareit.item.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemDto {
    private long id;
    private String name;
    private String description;
    private boolean available;
    private Long owner;
    private int numberOfTimesToRent;

    public static class Builder {
        private final ItemDto newItemDto;

        public Builder() {
            newItemDto = new ItemDto();
        }

        public ItemDto.Builder id(long id) {
            newItemDto.setId(id);
            return this;
        }

        public ItemDto.Builder name(String name) {
            newItemDto.setName(name);
            return this;
        }

        public ItemDto.Builder description(String description) {
            newItemDto.setDescription(description);
            return this;
        }

        public ItemDto.Builder isAvailable(boolean available) {
            newItemDto.setAvailable(available);
            return this;
        }

        public ItemDto.Builder owner(Long owner) {
            newItemDto.setOwner(owner);
            return this;
        }

        public ItemDto.Builder numberOfTimesToRent(int numberOfTimesToRent) {
            newItemDto.setNumberOfTimesToRent(numberOfTimesToRent);
            return this;
        }

        public ItemDto build() {
            return newItemDto;
        }
    }
}
