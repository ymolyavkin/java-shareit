package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.IncomingBookingDto;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NoneXSharerUserIdException;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.validator.BookingValidation;

import java.time.LocalDateTime;
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
    public Booking getBookingById(Long id) {
        return bookingRepository.getReferenceById(id);
    }

    @Override
    public List<BookingDto> getBookingsByBooker(Long bookerId) {
        List<Booking> bookings = bookingRepository.findAllByBookerId(bookerId);
        return bookings
                .stream()
                .map(BookingMapper::mapToBookingDto)
                .collect(Collectors.toList());
    }

    @Override
    public BookingDto addBooking(IncomingBookingDto incomingBookingDto) {
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
Booking booking0 = BookingMapper.mapToBooking(incomingBookingDto, item, booker);
        Booking booking = bookingRepository.save(BookingMapper.mapToBooking(incomingBookingDto, item, booker));

        return BookingMapper.mapToBookingDto(booking);
    }

    @Override
    public BookingDto updateBooking(IncomingBookingDto incomingBookingDto, Long bookingId, Long bookerId) {
        Booking booking = bookingRepository.getReferenceById(bookingId);
      /*  if (!bookingId.equals(booking.getBookerId())) {
            throw new OwnerMismatchException("Указанный пользователь не является владельцем вещи");
        }*/
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
        return BookingMapper.mapToBookingDto(booking);
    }

/*
    @Override
    public Optional<Item> getItemById(Long id) {
        return itemStorage.getItemById(id);
    }

    @Override
    public List<Item> searchItems(String keyword) {
        return itemStorage.searchItems(keyword);
    }*/
}
