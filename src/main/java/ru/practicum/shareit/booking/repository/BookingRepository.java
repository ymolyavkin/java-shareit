package ru.practicum.shareit.booking.repository;

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
    List<Booking> findAllByBooker_Id(Long bookerId, Sort sort);

    List<Booking> findAllByBooker_IdAndStartBeforeAndEndAfter(Long bookerId, LocalDateTime dateTime,
                                                              LocalDateTime dateTime1, Sort sort);

    List<Booking> findAllByBooker_IdAndEndBefore(Long bookerId, LocalDateTime dateTime, Sort sort);

    List<Booking> findAllByBooker_IdAndStartAfter(Long bookerId, LocalDateTime dateTime, Sort sort);

    List<Booking> findAllByBooker_IdAndStatus(Long bookerId, Status status, Sort sort);

    List<Booking> findByBooker_IdOrderByStartDesc(Long bookerId);
    Boolean existsByBooker_IdAndEndBeforeAndStatus(Long bookerId, LocalDateTime localDateTime, Status status);

    List<Booking> findByItem_Id(Long itemId);

    @Query(value = "select * from BOOKINGS\n" +
            "where booker_id = 1\n" +
            "  AND start_time<=DATEADD('HOUR',+3, NOW())\n" +
            "  AND end_time>=DATEADD('HOUR',+3, NOW())\n" +
            "order by start_time desc"
            , nativeQuery = true)
    List<Booking> findCurrent(Long userId);

    @Query(value = "select * from BOOKINGS\n" +
            "    where booker_id = 1\n" +
            "    AND end_time<DATEADD('HOUR',+3, NOW())\n" +
            "    order by start_time desc"
            , nativeQuery = true)
    List<Booking> findPast(Long userId);

    @Query(value = "select * from BOOKINGS\n" +
            "where booker_id = 1\n" +
            "  AND start_time>DATEADD('HOUR',+3, NOW())\n" +
            "order by start_time desc"
            , nativeQuery = true)
    List<Booking> findFuture(Long userId);

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
            "where item_id = ?1 and booker_id = ?2 and status = 'APPROVED'"
            , nativeQuery = true)
    int findByItemIdAndBookerId(Long itemId, Long bookerId);

    @Query(value = "select * from BOOKINGS where booker_id = 1? AND status='WAITING' order by start_time desc"
            , nativeQuery = true)
    List<Booking> findWaiting(Long userId);

    @Query(value = "select * from BOOKINGS where booker_id = 1? AND status='REJECTED' order by start_time desc"
            , nativeQuery = true)
    List<Booking> findRejected(Long userId);

    @Query(value = "select * from BOOKINGS where item_id=?1 order by start_time desc", nativeQuery = true)
    List<Booking> findByItemId(Long itemId);
 /*@Query ("SELECT H FROM HENTIY H WHERE H.C_ID IN: C_IDS")
 List <Guentity > FingHentityBycids (@param ("C_IDS") List <LONG> c_ids);*/

    List<Booking> findByItem_IdInOrderByStartDesc(List<Long> Ids);

    //           select count(*) from BOOKINGS where item_id=1 and ((end_time between ?2 and ?3) or (?3 between start_time and end_time))
    Optional<Booking> findFirstByItem_IdAndStartBeforeAndStatusOrderByStartDesc(Long itemId, LocalDateTime localDate,
                                                                                Status status);

    Optional<Booking> findFirstByItem_IdAndStartAfterAndStatusOrderByStartAsc(Long itemId, LocalDateTime localDate,
                                                                              Status status);
}
