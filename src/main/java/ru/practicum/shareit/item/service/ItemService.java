package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.comment.IncomingCommentDto;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.IncomingItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemLastNextDto;

import java.util.List;

public interface ItemService {
    ItemLastNextDto getItemById(Long id);

    List<ItemDto> searchItemsByText(String searchText);

    //  @Transactional
    //  ItemDto saveItem(IncomingItemDto incomingItemDto);

   // List<ItemWithDateDto> getItemsWithDateByUser(Long userId);

    List<ItemLastNextDto> getItemsLastNextBookingByUser(Long userId);

    /* @Override
             public List<Item> getItems() {
                 return itemStorage.getItems();
             }
        */
    List<ItemDto> getItemsByUser(Long userId);

    ItemDto addItem(IncomingItemDto incomingItemDto);

    /* @Override
                 public List<Item> getItems() {
                     return itemStorage.getItems();
                 }

                 @Override
                 public List<Item> getItems(Long userId) {
                     return itemStorage.getItems(userId);
                 }

                 @Override
                 public Item addItem(IncomingItemDto incomingItemDto) {
                     if (incomingItemDto.getOwnerId().equals(-1L)) {
                         throw new NoneXSharerUserIdException("Не указан владелец вещи");
                     }
                     return itemStorage.addItem(ItemMapper.toItem(incomingItemDto));
                 }
             */
    ItemDto updateItem(IncomingItemDto incomingItemDto, Long itemId, Long userId);

    CommentDto addComment(IncomingCommentDto incomingCommentDto, Long userId, Long itemId);
}
