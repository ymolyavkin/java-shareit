package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.dto.*;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NoneXSharerUserIdException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.OwnerMismatchException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.validator.BookingValidation;

import java.util.List;
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
            throw new OwnerMismatchException("Пользователь с id = {} не автор ,бронирования и не владелец вещи, данные о бронировании недоступны");
        }

        return BookingMapper.mapToBookingResponseDto(booking, item);
    }

    @Override
    public List<BookingResponseDto> getBookingsByBooker(Long bookerId) {
        User booker = userRepository.findById(bookerId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с id %d не найден", bookerId)));
        List<Booking> bookings = bookingRepository.findByBooker_Id(bookerId);
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

        BookingValidation.bookingIsValid(incomingBookingDto, item);

        Booking booking = bookingRepository.save(BookingMapper.mapToBooking(incomingBookingDto, item, booker));

        return BookingMapper.mapToBookingResponseDto(booking, item);
    }

    @Override
    public BookingResponseDto updateBooking(IncomingBookingDto incomingBookingDto, Long bookingId, Long bookerId) {
        Booking booking = bookingRepository.getReferenceById(bookingId);
        Item item = itemRepository.findById(booking.getItemId())
                .orElseThrow(() -> new NotFoundException(String.format("Вещь с id %d не найдена", booking.getItemId())));
        boolean needsToBeChanged = false;
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
        }
        return BookingMapper.mapToBookingResponseDto(booking, item);
    }

    @Override
    public BookingResponseDto approvingBooking(Long bookingId, Long ownerId, Boolean approved) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException(String.format("Бронирование с id %d не найдено", bookingId)));
        Item item = itemRepository.findById(booking.getItemId())
                .orElseThrow(() -> new NotFoundException(String.format("Вещь с id %d не найдена", booking.getItemId())));
        if (!ownerId.equals(item.getOwnerId())) {
            throw new OwnerMismatchException("Подтвержение может быть выполнено только владельцем вещи");
        }
        if (approved != null) {
            if (approved) {
                booking.setStatus(Status.APPROVED);
            } else {
                booking.setStatus(Status.REJECTED);
            }
            bookingRepository.saveAndFlush(booking);
        }
        return BookingMapper.mapToBookingResponseDto(booking, item);
    }

    @Override
    public List<BookingResponseDto> getBookingsByOwner(Long ownerId) {
        User booker = userRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с id %d не найден", ownerId)));

        List<Booking> bookings = bookingRepository.findByBooker_Id(ownerId);
        return bookings
                .stream()
                .map(booking -> BookingMapper.mapToBookingResponseDto(booking, booking.getItem()))
                .collect(Collectors.toList());
    }
}
