package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.item.dto.ItemWithIdAndNameDto;

import java.time.LocalDateTime;

@Builder
@Data
public class BookingWithItemIdAndNameDto {
    private long id;
    private LocalDateTime start;
    private LocalDateTime end;
  //  private Long itemId;
    private ItemWithIdAndNameDto item;
    private Long bookerId;
    private Status status;
}
