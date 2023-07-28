package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.util.Constants.SORT_BY_ASC;
import static ru.practicum.shareit.util.Constants.SORT_BY_DESC;

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

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
    @Override
    public List<BookingResponseDto> getBookingsByOwner(Long ownerId, StateRequest state, Integer from, Integer size) {
        LocalDateTime dateTimeNow = LocalDateTime.now();
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с id %d не найден", ownerId)));
        if (state == StateRequest.UNSUPPORTED_STATUS) {
            throw new UnsupportedStatusException("Unknown state: UNSUPPORTED_STATUS");
        }
        List<Long> itemIdsByOwner = itemsIdsByOwner(ownerId);

        Pageable pageable = PageRequest.of(from, size);
        Page<Booking> bookingPage = bookingRepository.findByItem_IdInOrderByStartDesc(itemIdsByOwner, pageable);
        List<Booking> bookingList = bookingPage.getContent();
        List<Booking> bookings;
        switch (state) {
            case ALL:
                bookings = bookingList;
                break;
            case CURRENT:
                bookings = bookingList
                        .stream()
                        .filter(booking -> booking.getStart().isBefore(dateTimeNow) && booking.getEnd().isAfter(dateTimeNow))
                        .collect(Collectors.toList());
                break;
            case FUTURE:
                bookings = bookingList
                        .stream()
                        .filter(booking -> booking.getStart().isAfter(dateTimeNow))
                        .collect(Collectors.toList());
                break;
            case PAST:
                bookings = bookingList
                        .stream()
                        .filter(booking -> booking.getEnd().isBefore(dateTimeNow))
                        .collect(Collectors.toList());
                break;
            case WAITING:
                bookings = bookingList
                        .stream()
                        .filter(booking -> booking.getStatus() == Status.WAITING)
                        .collect(Collectors.toList());
                break;
            case REJECTED:
                bookings = bookingList
                        .stream()
                        .filter(booking -> booking.getStatus() == Status.REJECTED)
                        .collect(Collectors.toList());
                break;
            default:
                throw new UnsupportedStatusException("Unknown state: UNSUPPORTED_STATUS");
        }
        return bookings
                .stream()
                .map(booking -> BookingMapper.mapToBookingResponseDto(booking, booking.getItem()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookingResponseDto> getBookingsByBooker(Long bookerId, StateRequest state, Integer from, Integer size) {
        LocalDateTime dateTimeNow = LocalDateTime.now();
        User booker = userRepository.findById(bookerId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с id %d не найден", bookerId)));
        if (state == StateRequest.UNSUPPORTED_STATUS) {
            throw new UnsupportedStatusException("Unknown state: UNSUPPORTED_STATUS");
        }
        List<Booking> bookings;
        switch (state) {
            case ALL:
                Pageable pageable = PageRequest.of(from / size, size, Sort.by("id").descending());
                Page<Booking> bookingPage = bookingRepository.findAllByBooker_Id(bookerId, pageable);

                bookings = bookingPage.getContent();
                break;
            case CURRENT:
                bookings = bookingRepository.findAllByBooker_IdAndStartBeforeAndEndAfter(bookerId, dateTimeNow, dateTimeNow, SORT_BY_DESC);
                break;
            case FUTURE:
                bookings = bookingRepository.findAllByBooker_IdAndStartAfter(bookerId, dateTimeNow, SORT_BY_DESC);
                break;
            case PAST:
                bookings = bookingRepository.findAllByBooker_IdAndEndBefore(bookerId, dateTimeNow, SORT_BY_DESC);
                break;
            case WAITING:
                bookings = bookingRepository.findAllByBooker_IdAndStatus(bookerId, Status.WAITING, SORT_BY_DESC);
                break;
            case REJECTED:
                bookings = bookingRepository.findAllByBooker_IdAndStatus(bookerId, Status.REJECTED, SORT_BY_DESC);
                break;
            default:
                bookings = Collections.emptyList();
        }
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

        boolean isOverlap = allBookingsByItem
                .stream()
                .map(booking -> isOverlapTime(booking, resultBooking))
                .reduce(Boolean::logicalOr).orElse(false);

        if (isOverlap) {
            throw new NotFoundException("Данная вещь на этот период недоступна");
        }
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

        booking.setStart(incomingBookingDto.getStart());
        booking.setEnd(incomingBookingDto.getEnd());
        booking.setStatus(incomingBookingDto.getStatus());
        bookingRepository.saveAndFlush(booking);

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