package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.NoneXSharerUserIdException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.OwnerMismatchException;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentRepository;
import ru.practicum.shareit.item.comment.IncomingCommentDto;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.dto.CommentMapper;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.booking.repository.BookingRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Override
    public ItemDto getItemById(Long id) {
        Item item = itemRepository.getReferenceById(id);
        return ItemMapper.mapToItemDto(item);
    }

   /* @Override
    public List<ItemWithDateDto> getItemsWithDateByUser(Long userId) {
        List<Item> items = itemRepository.findAllByOwnerId(userId);
        List<ItemWithDateDto> result = new ArrayList<>();

        for (Item item : items) {
            List<Booking> bookings = bookingRepository.findByItem_Id(item.getId());
            for (Booking booking : bookings) {
                result.add(ItemMapper.mapToItemWithDateDto(item, booking.getStart(), booking.getEnd()));
            }
        }

        return result;
    }*/

    @Override
    public List<ItemLastNextDto> getItemsLastNextBookingByUser(Long userId) {
        List<Item> items = itemRepository.findAllByOwnerId(userId);

        return items
                .stream()
                .map(item -> ItemMapper.mapToItemLastNextDto(item, bookingRepository.findLast(item.getId()), bookingRepository.findNext(item.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> getItemsByUser(Long userId) {
        List<Item> items = itemRepository.findAllByOwnerId(userId);
        return items
                .stream()
                .map(ItemMapper::mapToItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto addItem(IncomingItemDto incomingItemDto) {
        if (incomingItemDto.getOwnerId().equals(-1L)) {
            throw new NoneXSharerUserIdException("Не указан владелец вещи");
        }
        Long userId = incomingItemDto.getOwnerId();
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с id %d не найден", userId)));
        Item item = itemRepository.save(ItemMapper.mapToItem(incomingItemDto, owner));

        return ItemMapper.mapToItemDto(item);
    }

    @Override
    public ItemDto updateItem(IncomingItemDto incomingItemDto, Long itemId, Long userId) {
        Item item = itemRepository.getReferenceById(itemId);
        if (!userId.equals(item.getOwnerId())) {
            throw new OwnerMismatchException("Указанный пользователь не является владельцем вещи");
        }
        boolean needsToBeChanged = false;
        if (incomingItemDto.getName() != null && !incomingItemDto.getName().equals(item.getName())) {
            item.setName(incomingItemDto.getName());
            needsToBeChanged = true;
        }
        if (incomingItemDto.getDescription() != null && !incomingItemDto.getDescription().equals(item.getDescription())) {
            item.setDescription(incomingItemDto.getDescription());
            needsToBeChanged = true;
        }
        if (incomingItemDto.getAvailable() != null && !incomingItemDto.getAvailable().equals(item.getAvailable())) {
            item.setAvailable(incomingItemDto.getAvailable());
            needsToBeChanged = true;
        }
        if (needsToBeChanged) {
            itemRepository.saveAndFlush(item);
        }
        return ItemMapper.mapToItemDto(item);
    }

    @Override
    public List<ItemDto> searchItemsByText(String searchText) {
        List<Item> items = itemRepository.findByNameIsContainingIgnoreCaseOrDescriptionIsContainingIgnoreCase(searchText, searchText);

        return items
                .stream()
                .map(ItemMapper::mapToItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto addComment(IncomingCommentDto incomingCommentDto, Long userId, Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(String.format("Вещь с id %d не найдена", itemId)));
        int countBookings = bookingRepository.findByItemIdAndBookerId(itemId, userId);
        if (countBookings == 0) {
            throw new NotFoundException("Автор не брал данную вещь в аренду");
        }
        User author = item.getOwner();

        Comment comment = commentRepository.save(CommentMapper.mapToComment(incomingCommentDto, author, item));

        return CommentMapper.mapToCommentDto(comment);
    }
}
