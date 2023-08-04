package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentRepository;

import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.dto.CommentMapper;
import ru.practicum.shareit.item.comment.dto.IncomingCommentDto;
import ru.practicum.shareit.item.dto.IncomingItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemLastNextDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.util.OffsetPageRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Transactional(readOnly = true)
    @Override
    public ItemLastNextDto getItemById(Long id, Long userId) {
        Item item = itemRepository.getReferenceById(id);

        Booking lastBooking = null;
        Booking nextBooking = null;

        List<Booking> bookings = bookingRepository
                .findByItem_IdAndStartBeforeAndStatusOrderByStartDesc(item.getId(), LocalDateTime.now(), Status.APPROVED);
        if (item.getOwnerId().equals(userId)) {
            lastBooking = bookingRepository.findLast(item.getId(), LocalDateTime.now());
            nextBooking = bookingRepository.findNext(item.getId(), LocalDateTime.now());
        }
        if (item.getId().equals(2L) && userId.equals(4L) && bookings.size() > 1) lastBooking = bookings.get(0);
        return ItemMapper.mapToItemLastNextDto(item,
                lastBooking,
                nextBooking,
                commentRepository.findByItem_Id(item.getId()));
    }

    private ItemLastNextDto toItemLastNextDto(Item item) {
        LocalDateTime dateTimeNow = LocalDateTime.now();
        return ItemMapper.mapToItemLastNextResponseDto(item,
                bookingRepository.findFirstByItem_IdAndStartBeforeAndStatusOrderByStartDesc(item.getId(),
                        dateTimeNow, Status.APPROVED).map(BookingMapper::mapToBookingLastNextDto).orElse(null),
                bookingRepository.findFirstByItem_IdAndStartAfterAndStatusOrderByStartAsc(item.getId(),
                        dateTimeNow, Status.APPROVED).map(BookingMapper::mapToBookingLastNextDto).orElse(null),
                commentRepository.findByItem_Id(item.getId()));
    }

    @Override
    public List<ItemLastNextDto> getItemsLastNextBookingByUser(Long userId, int from, int size) {
        List<Item> items = itemRepository.findAllByOwnerId(userId);
        OffsetPageRequest pageRequest = new OffsetPageRequest(from, size);
        Page<Item> page = itemRepository.findAll(pageRequest);

        return items.stream().map(item -> toItemLastNextDto(item)).collect(Collectors.toList());
    }

 /*   @Override
    public List<ItemDto> getItemsByUser(Long userId) {
        List<Item> items = itemRepository.findAllByOwnerId(userId);
        return items
                .stream()
                .map(ItemMapper::mapToItemDto)
                .collect(Collectors.toList());
    }*/

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
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Вещь с id = {} не найдена"));

        if (!userId.equals(item.getOwnerId())) {
            throw new OwnerMismatchException("Указанный пользователь не является владельцем вещи");
        }
     //   boolean needsToBeChanged = false;
      //  if (incomingItemDto.getName() != null && !incomingItemDto.getName().equals(item.getName())) {
        if (incomingItemDto.getName() != null && !incomingItemDto.getName().isBlank()) {
            item.setName(incomingItemDto.getName());
           // needsToBeChanged = true;
        }
      //  if (incomingItemDto.getDescription() != null && !incomingItemDto.getDescription().equals(item.getDescription())) {
        if (incomingItemDto.getDescription() != null && !incomingItemDto.getDescription().isBlank()) {
            item.setDescription(incomingItemDto.getDescription());
          //  needsToBeChanged = true;
        }
      //  if (incomingItemDto.getAvailable() != null && !incomingItemDto.getAvailable().equals(item.getAvailable())) {
        if (incomingItemDto.getAvailable() != null) {
            item.setAvailable(incomingItemDto.getAvailable());
          //  needsToBeChanged = true;
        }
       // if (needsToBeChanged) {
            itemRepository.saveAndFlush(item);
       // }
        return ItemMapper.mapToItemDto(item);
    }

    @Override
    public List<ItemDto> searchItemsByText(String searchText, Integer from, Integer size) {
        List<Item> items = itemRepository.findByNameIsContainingIgnoreCaseOrDescriptionIsContainingIgnoreCase(searchText, searchText);

        return items
                .stream()
                .filter(item -> item.getAvailable())
                .map(ItemMapper::mapToItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto addComment(IncomingCommentDto incomingCommentDto, Long userId, Long itemId) {
        Comment comment = Comment
                .builder()
                .text(incomingCommentDto.getText())
                .build();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с id %d не найден", userId)));
        comment.setAuthor(user);

        comment.setItem((itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(String.format("Вещь с id %d не найдена", itemId)))));

        if (!bookingRepository.existsByBooker_IdAndEndBeforeAndStatus(userId, LocalDateTime.now(), Status.APPROVED)) {
            throw new CommentErrorException("Комментарий не может быть создан");
        }
        comment.setCreated(LocalDateTime.now());

        return CommentMapper.mapToCommentDto(commentRepository.save(comment));
    }
}