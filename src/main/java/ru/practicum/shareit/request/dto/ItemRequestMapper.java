package ru.practicum.shareit.request.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

@UtilityClass
public class ItemRequestMapper {
    public static ItemRequestAnswerDto mapToItemRequestAnswerDto(ItemRequest itemRequest, List<ItemAnswerToRequestDto> answers) {
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

        return ItemRequestAnswerDto.builder()
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated())
                .answersToRequest(answers)
                .build();
    }
}
