package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.StateRequest;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.dto.*;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.util.Time;
import ru.practicum.shareit.validator.BookingValidation;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;

    @Override
    public List<Booking> getAll() {
        return bookingRepository.findAll();
    }

    @Override
    public BookingResponseDto getBookingById(Long id, Long userId) {
        Booking booking = bookingRepository.getReferenceById(id);
        Item item = itemRepository.findById(booking.getItemId())
                .orElseThrow(() -> new NotFoundException(String.format("Вещь с id %d не найдена", booking.getItemId())));
        if (!booking.getBookerId().equals(userId) && !item.getOwnerId().equals(userId)) {
            throw new NotFoundException(String.format("Пользователь с id %d не не автор бронирования и не владелец вещи, данные о бронировании недоступны", userId));
        }

        return BookingMapper.mapToBookingResponseDto(booking, item);
    }

    private List<Long> itemsIdsByOwner(Long ownerId) {
        return itemRepository.findItemIdsByOwnerId(ownerId);
    }

    private List<Item> itemsByOwner(Long ownerId) {
        return itemRepository.findAllByOwnerId(ownerId);
    }

    @Override
    public List<BookingResponseDto> getBookingsByOwner(Long ownerId, StateRequest state) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с id %d не найден", ownerId)));
        if (state == StateRequest.UNSUPPORTED_STATUS) {
            throw new UnsupportedStatusException("Unknown state: UNSUPPORTED_STATUS");
        }

        List<Long> itemIdsByOwner = itemsIdsByOwner(ownerId);

        List<Booking> bookingList = bookingRepository.findByItem_IdInOrderByStartDesc(itemIdsByOwner);
        /*List<Item> items = itemsByOwner(ownerId);
        List<Booking> v  = itemIdsByOwner.stream().map(itemId -> this.add(bookingRepository.findByItemId(itemId))).collect(Collectors.toList());
      //  List<Booking> v = items.stream().map(item -> item.getBookings()).sorted().toList();
        Item item = itemRepository.getReferenceById(itemIdsByOwner.get(0));*/

        List<Booking> bookings = switch (state) {
            case ALL -> bookingList;
            case CURRENT -> bookingList
                    .stream()
                    .filter(booking -> booking.getStart().isBefore(LocalDateTime.now()) && booking.getEnd().isAfter(LocalDateTime.now()))
                    .collect(Collectors.toList());
            case FUTURE -> bookingList
                    .stream()
                    .filter(booking -> booking.getStart().isAfter(LocalDateTime.now()))
                    .collect(Collectors.toList());
            case WAITING -> bookingList
                    .stream()
                    .filter(booking -> booking.getStatus() == Status.WAITING)
                    .collect(Collectors.toList());
            case REJECTED -> bookingList
                    .stream()
                    .filter(booking -> booking.getStatus() == Status.REJECTED)
                    .collect(Collectors.toList());
            default -> throw new UnsupportedStatusException("Unknown state: UNSUPPORTED_STATUS");
        };
        // List<Booking> bookings = bookingRepository.findByBooker_IdOrderByStartDesc(ownerId);

        return bookings
                .stream()
                .map(booking -> BookingMapper.mapToBookingResponseDto(booking, booking.getItem()))
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingResponseDto> getBookingsByBooker(Long bookerId, StateRequest state) {
        User booker = userRepository.findById(bookerId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с id %d не найден", bookerId)));
       /* List<Booking> bookings = switch (state) {
            case ALL, CURRENT, FUTURE, WAITING, REJECTED -> bookingRepository.findByBooker_IdOrderByStartDesc(bookerId);
            default -> throw new UnsupportedStatusException ("Unknown state: UNSUPPORTED_STATUS");
        };*/

        if (state == StateRequest.UNSUPPORTED_STATUS) {
            throw new UnsupportedStatusException("Unknown state: UNSUPPORTED_STATUS");
        }
        List<Booking> bookings = bookingRepository.findByBooker_IdOrderByStartDesc(bookerId);
        return bookings
                .stream()
                .map(booking -> BookingMapper.mapToBookingResponseDto(booking, booking.getItem()))
                .collect(Collectors.toList());
    }

    @Override
    public BookingResponseDto addBooking(IncomingBookingDto incomingBookingDto) {
        if (incomingBookingDto.getBookerId().equals(-1L)) {
            throw new NoneXSharerUserIdException("Не указан инициатор бронирования");
        }
        Long bookerId = incomingBookingDto.getBookerId();
        Long itemId = incomingBookingDto.getItemId();
        User booker = userRepository.findById(bookerId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с id %d не найден", bookerId)));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(String.format("Вещь с id %d не найдена", itemId)));
        if (bookerId.equals(item.getOwnerId())) {
            throw new NotFoundException("Инициатор бронирования и владелец запрашиваемой вещи совпадают");
        }
        BookingValidation.bookingIsValid(incomingBookingDto, item);
        Booking resultBooking = BookingMapper.mapToBooking(incomingBookingDto, item, booker);
        List<Booking> allBookingsByItem = bookingRepository.findByItem_Id(itemId);
        //stream.map(Whatever::someCheck).reduce(Boolean::logicalAnd).orElse(false);
        boolean isOverlap = allBookingsByItem
                .stream()
                .map(booking -> isOverlapTime(booking, resultBooking))
                .reduce(Boolean::logicalOr).orElse(false);
        /*List<Booking> overlapBookings = allBookingsByItem.stream().findFirst().
                .stream()
                .filter(booking -> isOverlapTime(booking, resultBooking))
                .collect(Collectors.toList());*/

        if (isOverlap) {
            throw new NotFoundException("Данная вещь на этот период недоступна");
        }
        //  Booking booking = bookingRepository.save(BookingMapper.mapToBooking(incomingBookingDto, item, booker));
        Booking booking = bookingRepository.save(resultBooking);

        return BookingMapper.mapToBookingResponseDto(booking, item);
    }

    public boolean isOverlapTime(Booking one, Booking two) {
        return Time.isOverlapping(one.getStart(), one.getEnd(), two.getStart(), two.getEnd());
    }

    @Override
    public BookingResponseDto updateBooking(IncomingBookingDto incomingBookingDto, Long bookingId, Long bookerId) {
        Booking booking = bookingRepository.getReferenceById(bookingId);
        Item item = itemRepository.findById(booking.getItemId())
                .orElseThrow(() -> new NotFoundException(String.format("Вещь с id %d не найдена", booking.getItemId())));
        boolean needsToBeChanged = false;
       /* Status currentStatus = booking.getStatus();
        Status newStatus = incomingBookingDto.getStatus();*/
        if (incomingBookingDto.getStart() != null && !incomingBookingDto.getStart().equals(booking.getStart())) {
            booking.setStart(incomingBookingDto.getStart());
            needsToBeChanged = true;
        }
        if (incomingBookingDto.getEnd() != null && !incomingBookingDto.getEnd().equals(booking.getEnd())) {
            booking.setEnd(incomingBookingDto.getEnd());
            needsToBeChanged = true;
        }
        if (incomingBookingDto.getStatus() != null && !incomingBookingDto.getStatus().equals(booking.getStatus())) {
            booking.setStatus(incomingBookingDto.getStatus());
            needsToBeChanged = true;
        }
        if (needsToBeChanged) {
            bookingRepository.saveAndFlush(booking);
        } /*else {
            throw new BadRequestException("Nothing to change");
        }*/
        return BookingMapper.mapToBookingResponseDto(booking, item);
    }

    @Override
    public BookingResponseDto approvingBooking(Long bookingId, Long ownerId, Boolean approved) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException(String.format("Бронирование с id %d не найдено", bookingId)));
        Item item = itemRepository.findById(booking.getItemId())
                .orElseThrow(() -> new NotFoundException(String.format("Вещь с id %d не найдена", booking.getItemId())));
        if (!ownerId.equals(item.getOwnerId())) {
            throw new NotFoundException("Подтвержение может быть выполнено только владельцем вещи");
        }
        if (approved != null) {
            if (approved) {
                if (booking.getStatus() == Status.APPROVED) {
                    throw new BadRequestException("Status is already approved");
                }
                booking.setStatus(Status.APPROVED);
            } else {
                booking.setStatus(Status.REJECTED);
            }
            bookingRepository.saveAndFlush(booking);
        }
        return BookingMapper.mapToBookingResponseDto(booking, item);
    }


}


/*
// сначала создаём описание сортировки по полю id
        Sort sortById = Sort.by(Sort.Direction.ASC, "id");
                // затем создаём описание первой "страницы" размером 32 элемента
        Pageable page = PageRequest.of(0, 32, sortById);
        do {
                        // запрашиваем у базы данных страницу с данными
            Page<User> userPage = repository.findAll(page);
                        // результат запроса получаем с помощью метода getContent()
            userPage.getContent().forEach(user -> {
                // проверяем пользователей
            });
                        // для типа Page проверяем, существует ли следующая страница
            if(userPage.hasNext()){
                                // если следующая страница существует, создаём её описание, чтобы запросить на следующей итерации цикла
                page = PageRequest.of(userPage.getNumber() + 1, userPage.getSize(), userPage.getSort()); // или для простоты -- userPage.nextOrLastPageable()
            } else {
                page = null;
            }
        } while (page != null);
    }
 */