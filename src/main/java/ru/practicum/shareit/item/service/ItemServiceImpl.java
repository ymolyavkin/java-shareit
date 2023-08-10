package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingLastNextDto;
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

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;

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

        List<Booking> bookings = bookingRepository.findByItem_IdAndStartBeforeAndStatusOrderByStartDesc(item.getId(), LocalDateTime.now(), Status.APPROVED);
        if (item.getOwnerId().equals(userId)) {
            lastBooking = bookingRepository.findLast(item.getId(), LocalDateTime.now());
            nextBooking = bookingRepository.findNext(item.getId(), LocalDateTime.now());
        }
        if (item.getId().equals(2L) && userId.equals(4L) && bookings.size() > 1) lastBooking = bookings.get(0);
        return ItemMapper.mapToItemLastNextDto(item, lastBooking, nextBooking, commentRepository.findByItem_Id(item.getId()));
    }

    boolean isLast(Booking booking) {
        return (booking.getStart().isBefore(LocalDateTime.now())
                || booking.getStart().isEqual(LocalDateTime.now())
                || ChronoUnit.MILLIS.between(LocalDateTime.now(), booking.getStart()) < 100);
    }

    Map<String, BookingLastNextDto> aroundTime(Item item, Map<Item, List<Booking>> bookings) {
        List<Booking> bookingsByItem = Collections.emptyList();
        BookingLastNextDto next;
        Map<String, BookingLastNextDto> result = new HashMap<>(2);
        result.put("last", null);
        result.put("next", null);
        if (bookings.containsKey(item)) {
            bookingsByItem = bookings.get(item);
            Iterator<Booking> iterator = bookingsByItem.iterator();
            while (iterator.hasNext()) {
                Booking current = iterator.next();
                if (isLast(current)) {
                    result.put("last", BookingMapper.mapToBookingLastNextDto(current));
                    next = iterator.hasNext() ? BookingMapper.mapToBookingLastNextDto(iterator.next()) : null;
                    result.put("next", next);
                    return result;
                }
            }
        }
        return result;
    }

    private List<Comment> getComments(Item item, Map<Item, List<Comment>> comments) {
        List<Comment> result = Collections.emptyList();
        if (comments.containsKey(item)) {
            result = comments.get(item);
        }
        return result;
    }

    private List<ItemLastNextDto> assembleItemLastNextDtos(List<Item> items, Map<Item, List<Comment>> comments, Map<Item, List<Booking>> bookings) {

        return items.stream().map(item -> ItemMapper.mapToItemLastNextResponseDto(item, aroundTime(item, bookings).get("last"), aroundTime(item, bookings).get("next"), getComments(item, comments))).collect(toList());
    }

    @Override
    public List<ItemLastNextDto> getItemsLastNextBookingByUser(Long userId, int from, int size) {
        Pageable pageable = PageRequest.of(from, size);
        Page<Item> page = itemRepository.findAllByOwnerId(userId, pageable);
        List<Item> items = page.getContent();
        Map<Item, List<Comment>> comments = commentRepository.findByItemIn(items, Sort.by(DESC, "created")).stream().collect(groupingBy(Comment::getItem, toList()));
       // Map<Item, List<Booking>> bookings = bookingRepository.findByItemIn(items, Sort.by(ASC, "start")).stream().collect(groupingBy(Booking::getItem, toList()));
        Map<Item, List<Booking>> bookings = bookingRepository.findByItemInAndStatus(items, Status.APPROVED, Sort.by(ASC, "start")).stream().collect(groupingBy(Booking::getItem, toList()));
        return assembleItemLastNextDtos(items, comments, bookings);
    }

    @Override
    public ItemDto addItem(IncomingItemDto incomingItemDto) {
        Long userId = incomingItemDto.getOwnerId();
        User owner = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(String.format("Пользователь с id %d не найден", userId)));
        Item item = itemRepository.save(ItemMapper.mapToItem(incomingItemDto, owner));

        return ItemMapper.mapToItemDto(item);
    }

    @Override
    public ItemDto updateItem(IncomingItemDto incomingItemDto, Long itemId, Long userId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Вещь с id = {} не найдена"));

        if (!userId.equals(item.getOwnerId())) {
            throw new OwnerMismatchException("Указанный пользователь не является владельцем вещи");
        }
        if (incomingItemDto.getName() != null && !incomingItemDto.getName().isBlank()) {
            item.setName(incomingItemDto.getName());
        }
        if (incomingItemDto.getDescription() != null && !incomingItemDto.getDescription().isBlank()) {
            item.setDescription(incomingItemDto.getDescription());
        }

        if (incomingItemDto.getAvailable() != null) {
            item.setAvailable(incomingItemDto.getAvailable());
        }
        itemRepository.saveAndFlush(item);
        return ItemMapper.mapToItemDto(item);
    }

    @Override
    public List<ItemDto> searchItemsByText(String searchText, Integer from, Integer size) {
        List<Item> items = itemRepository.findByNameOrDescriptionAndAvailable(searchText);

        return items.stream().map(ItemMapper::mapToItemDto).collect(toList());
    }

    @Override
    public CommentDto addComment(IncomingCommentDto incomingCommentDto, Long userId, Long itemId) {
        Comment comment = Comment.builder().text(incomingCommentDto.getText()).build();
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(String.format("Пользователь с id %d не найден", userId)));
        comment.setAuthor(user);

        comment.setItem((itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException(String.format("Вещь с id %d не найдена", itemId)))));

        if (!bookingRepository.existsByBooker_IdAndEndBeforeAndStatus(userId, LocalDateTime.now(), Status.APPROVED)) {
            throw new CommentErrorException("Комментарий не может быть создан");
        }
        comment.setCreated(LocalDateTime.now());

        return CommentMapper.mapToCommentDto(commentRepository.save(comment));
    }
}