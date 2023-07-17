package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.BadRequestException;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.util.Constants.DATE_TIME_NOW;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    /*@Override
    public ItemDto getItemById(Long id) {
        Item item = itemRepository.getReferenceById(id);
        return ItemMapper.mapToItemDto(item);
    }*/
    @Override
    public ItemLastNextDto getItemById(Long id, Long userId) {
        Item item = itemRepository.getReferenceById(id);
        Booking lastBooking = null;
        Booking nextBooking = null;
        if (item.getOwnerId().equals(userId)) {
            lastBooking = bookingRepository.findLast(item.getId(), DATE_TIME_NOW);
            nextBooking = bookingRepository.findNext(item.getId(), DATE_TIME_NOW);
        }
        return ItemMapper.mapToItemLastNextDto(item,
                lastBooking,
                nextBooking,
                commentRepository.findByItem_Id(item.getId()));
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
   /*Map<Long, BookingItemDto> lastBookings = bookingRepository.findFirstByItemIdInAndStartLessThanEqualAndStatus(idItems, LocalDateTime.now(), Status.APPROVED, Sort.by(DESC, "start"))
           .stream()
           .map(BookingMapper::bookingToItemBookingDto)
           .collect(Collectors.toMap(BookingItemDto::getItemId, Function.identity()));
        itemDtoList.forEach(i -> i.setLastBooking(lastBookings.get(i.getId())));

    //5) Для каждого элемента ItemDto в списке itemDtoList устанавливается последнее бронирование,
    // используя значение из словаря lastBookings по его идентификатору.
    Map<Long, BookingItemDto> nextBookings = bookingRepository.findFirstByItemIdInAndStartAfterAndStatus(
                    idItems, LocalDateTime.now(), Status.APPROVED, Sort.by(Sort.Direction.ASC, "start"))
            .stream()
            .map(BookingMapper::bookingToItemBookingDto)
            .collect(Collectors.toMap(BookingItemDto::getItemId, Function.identity()));*/
    @Override
    public List<ItemLastNextDto> getItemsLastNextBookingByUser(Long userId) {
        List<Item> items = itemRepository.findAllByOwnerId(userId);

        return items
                .stream()
                .map(item -> ItemMapper.mapToItemLastNextDto(item,
                        //   bookingRepository.findLast(item.getId(), DATE_TIME_NOW),
                        bookingRepository.findFirstByItem_IdAndStartBeforeAndStatusOrderByStartDesc(item.getId(), DATE_TIME_NOW, Status.APPROVED).get(),
                        //  bookingRepository.findNext(item.getId(), DATE_TIME_NOW),
                        bookingRepository.findFirstByItem_IdAndStartAfterAndStatusOrderByStartAsc(item.getId(), DATE_TIME_NOW, Status.APPROVED).get(),
                        commentRepository.findByItem_Id(item.getId())))
                .collect(Collectors.toList());
    }

    private Booking getLastBooking(Item item) {
        List<Booking> bookings = bookingRepository.findByItem_Id(item.getId());
        List<Booking> currentBooking = bookings
                .stream()
                .filter(booking -> booking.getStart().isBefore(DATE_TIME_NOW) && booking.getEnd().isAfter(DATE_TIME_NOW))
                .collect(Collectors.toList());
        if (currentBooking.size() > 0) {
            return currentBooking.get(0);
        }
        return bookingRepository.findLast(item.getId(), DATE_TIME_NOW);
    }

    private Booking getNextBooking(Item item) {
        List<Booking> bookings = bookingRepository.findByItem_Id(item.getId());
        List<Booking> currentBooking = bookings
                .stream()
                .filter(booking -> booking.getStart().isBefore(LocalDateTime.now()) && booking.getEnd().isAfter(LocalDateTime.now()))
                .collect(Collectors.toList());
        if (currentBooking.size() > 0) {
            return currentBooking.get(0);
        }
        return bookingRepository.findNext(item.getId(), DATE_TIME_NOW);
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
        // Item item = itemRepository.getReferenceById(itemId);
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Вещь с id = {} не найдена"));
        // User user = item.getOwner();

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
                .filter(item -> item.getAvailable())
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
        if (!bookingRepository.existsByBooker_IdAndEndBeforeAndStatus(userId, DATE_TIME_NOW, Status.APPROVED)) {
            throw new BadRequestException("Комментарий не может быть создан");
        }
/*
Comment comment = Comment
                .builder()
                .text(commentDto.getText())
                .build();
        comment.setAuthor(userDtotoUser(userService.getUserById(userId)));

        comment.setItem((itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item  hasn't be found"))));

        comment.setAuthor(userDtotoUser(userService.getUserById(userId)));
        if (!bookingRepository.existsByBookerIdAndEndBeforeAndStatus(userId, LocalDateTime.now(), Status.APPROVED)) {
            throw new NotAvailableException("Comment can't be created");
        }
        comment.setCreated(LocalDateTime.now());
        log.info("Comment has been added");
        return commentToCommentDto(commentRepository.save(comment));
 */
        Comment comment = commentRepository.save(CommentMapper.mapToComment(incomingCommentDto, author, item));

        return CommentMapper.mapToCommentDto(comment);
    }
}
