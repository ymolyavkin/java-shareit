package ru.practicum.shareit.request.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@UtilityClass
public class ItemRequestMapper {
    public static ItemRequestWithAnswersDto mapToItemRequestAnswerDto(ItemRequest itemRequest, List<ItemAnswerToRequestDto> answers) {
        /*ItemAnswerToRequestDto itemAnswerToRequestDto = new ItemAnswerToRequestDto() {
            private Long itemId= item.getId();
            private String itemName= item.getName();
            private String itemDescription=item.getDescription();
            private Long requestId=itemRequest.getId();
            private boolean itemAvailable= item.getAvailable();

            public Long getItemId() {
                return itemId;
            }

            public String getItemName() {
                return itemName;
            }
            public String getItemDescription() {
                return itemDescription;
            }
            public Long getRequestId() {
                return requestId;
            }
            public boolean getItemAvailable() {
                return itemAvailable;
            }
        };*/

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
        ItemRequestResponseDto itemRequestResponseDto = new ItemRequestResponseDto(itemRequest.getId(), itemRequest.getDescription(), itemRequest.getCreated());

        return itemRequestResponseDto;
    }

    public static ItemAnswerToRequestDto mapToItemAnswerToRequestDto(Item item) {
        return ItemAnswerToRequestDto.builder()
                .itemId(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .requestId(item.getRequestId())
                .build();
    }
}
