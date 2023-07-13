package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

@Component
public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query(value = "select * " +
            "from BOOKINGS " +
            "where booker_id = ?1", nativeQuery = true)

    List<Booking> findAllByBookerId(Long bookerId);
    @Query(value = "select * " +
            "from BOOKINGS " +
            "where item_id = ?1", nativeQuery = true)
    List<Booking> findAllByItemId(Long itemId);
}
