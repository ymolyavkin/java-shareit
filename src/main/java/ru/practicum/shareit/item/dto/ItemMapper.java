package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.booking.dto.BookingLastNextDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Slf4j
@UtilityClass
public class ItemMapper {

    public static ItemDto mapToItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .isAvailable(item.getAvailable())
                .ownerId(item.getOwnerId())
                .numberOfTimesToRent(item.getNumberOfTimesToRent())
                .build();
    }

    public static Item mapToItem(IncomingItemDto incomingItemDto, User owner) {
        Item item = new Item();
        item.setName(incomingItemDto.getName());
        item.setDescription(incomingItemDto.getDescription());
        item.setAvailable(incomingItemDto.getAvailable());
        item.setOwner(owner);

        return item;
    }

    public static ItemWithDateDto mapToItemWithDateDto(Item item, LocalDateTime start, LocalDateTime end) {
        return ItemWithDateDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .isAvailable(item.getAvailable())
                .ownerId(item.getOwnerId())
                .start(start)
                .end(end)
                .numberOfTimesToRent(item.getNumberOfTimesToRent())
                .build();
    }

    public static ItemLastNextDto mapToItemLastNextDto(Item item, Booking lastBooking, Booking nextBooking) {
        @AllArgsConstructor
        @Getter
        class NearestBookingDto implements BookingLastNextDto {
            private final Long id;
            private final Long bookerId;
        }
        Long lastBookingId = (lastBooking == null) ? null : lastBooking.getId();
        Long nextBookingId = (nextBooking == null) ? null : nextBooking.getId();

        Long lastBookerId = (lastBooking == null) ? null : lastBooking.getBookerId();
        Long nextBookerId = (nextBooking == null) ? null : nextBooking.getBookerId();

        return ItemLastNextDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .isAvailable(item.getAvailable())
                .lastBooking(new NearestBookingDto(lastBookingId, lastBookerId))
                .nextBooking(new NearestBookingDto(nextBookingId, nextBookerId))
                .build();
    }
}


