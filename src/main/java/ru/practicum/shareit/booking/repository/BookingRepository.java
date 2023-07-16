package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;
import java.util.Set;

@Component
public interface BookingRepository extends JpaRepository<Booking, Long> {
    /*@Query(value = "select * " +
            "from BOOKINGS " +
            "where booker_id = ?1", nativeQuery = true)*/
  //  List<Booking> findByBooker_IdAndStatusIsRejectedOrderByStartDesc(Long bookerId);
    List<Booking> findByBooker_IdOrderByStartDesc(Long bookerId);
   /* @Query(value = "select * " +
            "from BOOKINGS " +
            "where item_id = ?1", nativeQuery = true)*/
    List<Booking> findByItem_Id(Long itemId);

    @Query(value="select * from BOOKINGS\n" +
            "where booker_id = 1\n" +
            "  AND start_time<=DATEADD('HOUR',+3, NOW())\n" +
            "  AND end_time>=DATEADD('HOUR',+3, NOW())\n" +
            "order by start_time desc"
            , nativeQuery = true)
    List<Booking> findCurrent(Long userId);
    @Query(value="select * from BOOKINGS\n" +
            "    where booker_id = 1\n" +
            "    AND end_time<DATEADD('HOUR',+3, NOW())\n" +
            "    order by start_time desc"
            , nativeQuery = true)

    List<Booking> findPast(Long userId);

    @Query(value="select * from BOOKINGS\n" +
            "where booker_id = 1\n" +
            "  AND start_time>DATEADD('HOUR',+3, NOW())\n" +
            "order by start_time desc"
            , nativeQuery = true)

    List<Booking> findFuture(Long userId);
    @Query(value="select * " +
            "from BOOKINGS " +
            "where id = ?1 and status = 'APPROVED' " +
            "order by start_time " +
            "limit 1", nativeQuery = true)
    Booking findLast(Long itemId);
    @Query(value="select * " +
            "from BOOKINGS " +
            "where id = ?1 and status = 'APPROVED' " +
            "order by start_time desc " +
            "limit 1", nativeQuery = true)
    Booking findNext(Long itemId);
    @Query(value="select count(*) " +
            "from BOOKINGS " +
            "where item_id = ?1 and booker_id = ?2 and status = 'APPROVED'"
            , nativeQuery = true)
    int findByItemIdAndBookerId(Long itemId, Long bookerId);

 @Query(value="select * from BOOKINGS where booker_id = 1? AND status='WAITING' order by start_time desc"
         , nativeQuery = true)
 List<Booking> findWaiting(Long userId);

 @Query(value="select * from BOOKINGS where booker_id = 1? AND status='REJECTED' order by start_time desc"
         , nativeQuery = true)
 List<Booking> findRejected(Long userId);

 @Query(value="select * from BOOKINGS where item_id=?1 order by start_time desc", nativeQuery = true)

 List<Booking> findByItemId(Long itemId);
 /*@Query ("SELECT H FROM HENTIY H WHERE H.C_ID IN: C_IDS")
 List <Guentity > FingHentityBycids (@param ("C_IDS") List <LONG> c_ids);*/

 Set<Booking> findByItem_IdIn(Set<Long> Ids);
}
