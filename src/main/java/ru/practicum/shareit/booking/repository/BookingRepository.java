package ru.practicum.shareit.booking.repository;

<<<<<<< HEAD
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
=======
>>>>>>> 6cc5d081d5fc2f68fbe70910fb5eb6895ef10748
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByItem_IdAndStartBeforeAndStatusOrderByStartDesc(Long itemId, LocalDateTime localDate,
                                                                       Status status);

    List<Booking> findAllByBooker_Id(Long bookerId, Sort sort);
<<<<<<< HEAD
    Page<Booking> findAllByBooker_Id(Long bookerId, Pageable pageable);
=======
>>>>>>> 6cc5d081d5fc2f68fbe70910fb5eb6895ef10748

    List<Booking> findAllByBooker_IdAndStartBeforeAndEndAfter(Long bookerId, LocalDateTime dateTime,
                                                              LocalDateTime dateTime1, Sort sort);

    List<Booking> findAllByBooker_IdAndEndBefore(Long bookerId, LocalDateTime dateTime, Sort sort);

    List<Booking> findAllByBooker_IdAndStartAfter(Long bookerId, LocalDateTime dateTime, Sort sort);

    List<Booking> findAllByBooker_IdAndStatus(Long bookerId, Status status, Sort sort);

    List<Booking> findByBooker_IdOrderByStartDesc(Long bookerId);

    Boolean existsByBooker_IdAndEndBeforeAndStatus(Long bookerId, LocalDateTime localDateTime, Status status);

    List<Booking> findByItem_Id(Long itemId);


    @Query(value = "select * " +
            "from BOOKINGS " +
            "where ITEM_ID = ?1 and start_time <= ?2 and status = 'APPROVED' " +
            "order by start_time " +
            "limit 1", nativeQuery = true)
    Booking findLast(Long itemId, LocalDateTime dateTimeNow);

    @Query(value = "select * " +
            "from BOOKINGS " +
            "where ITEM_ID = ?1 and start_time >= ?2 and status = 'APPROVED' " +
            "order by start_time limit 1", nativeQuery = true)
    Booking findNext(Long itemId, LocalDateTime dateTimeNow);

    @Query(value = "select count(*) " +
            "from BOOKINGS " +
            "where item_id = ?1 and booker_id = ?2 and status = 'APPROVED'",
            nativeQuery = true)
    int findByItemIdAndBookerId(Long itemId, Long bookerId);

    List<Booking> findByItem_IdInOrderByStartDesc(List<Long> ids);
<<<<<<< HEAD
    Page<Booking> findByItem_IdInOrderByStartDesc(List<Long> ids, Pageable pageable);
=======
>>>>>>> 6cc5d081d5fc2f68fbe70910fb5eb6895ef10748

    Optional<Booking> findFirstByItem_IdAndStartBeforeAndStatusOrderByStartDesc(Long itemId, LocalDateTime localDate,
                                                                                Status status);

    Optional<Booking> findFirstByItem_IdAndStartAfterAndStatusOrderByStartAsc(Long itemId, LocalDateTime localDate,
                                                                              Status status);
<<<<<<< HEAD
}
=======
}
>>>>>>> 6cc5d081d5fc2f68fbe70910fb5eb6895ef10748
