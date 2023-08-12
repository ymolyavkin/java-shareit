package ru.practicum.shareitserver.request.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareitserver.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@UtilityClass
public class ItemRequestMapper {
    public static ItemRequestWithAnswersDto mapToItemRequestAnswerDto(ItemRequest itemRequest, List<ItemAnswerToRequestDto> answers) {
        return ItemRequestWithAnswersDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated())
                .items(answers)
                .build();
    }

    public static ItemRequest mapToItemRequest(IncomingItemRequestDto incomingItemRequestDto, User requester) {
        return new ItemRequest(incomingItemRequestDto.getDescription(), requester, LocalDateTime.now());
    }

    public static ItemRequestResponseDto mapToItemRequestResponseDto(ItemRequest itemRequest) {
        ItemRequestResponseDto itemRequestResponseDto =
                new ItemRequestResponseDto(itemRequest.getId(), itemRequest.getDescription(), itemRequest.getCreated());

        return itemRequestResponseDto;
    }

    public static ItemAnswerToRequestDto mapToItemAnswerToRequestDto(Item item) {
        return ItemAnswerToRequestDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .requestId(item.getRequestId())
                .build();
    }
}
