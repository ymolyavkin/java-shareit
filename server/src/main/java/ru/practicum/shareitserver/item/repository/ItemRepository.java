package ru.practicum.shareitserver.item.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import ru.practicum.shareitserver.item.model.Item;

import java.util.List;

@Component
public interface ItemRepository extends JpaRepository<Item, Long> {
    @Query(value = "select * " +
            "from ITEMS " +
            "where owner_id = ?1", nativeQuery = true)
    List<Item> findAllByOwnerId(Long userId);

    @Query(value = "select * " +
            "from ITEMS " +
            "where owner_id = ?1", nativeQuery = true)
    Page<Item> findAllByOwnerId(Long userId, Pageable pageable);

    @Query("SELECT i FROM Item AS i " +
            "WHERE (LOWER(i.name) LIKE LOWER(concat('%', :text, '%')) " +
            "OR LOWER(i.description) LIKE LOWER(concat('%', :text, '%'))) AND i.available=true")
    List<Item> findByNameOrDescriptionAndAvailable(@Param("text") String text);

    @Query(value = "SELECT id FROM ITEMS WHERE owner_id = ?1", nativeQuery = true)
    List<Long> findItemIdsByOwnerId(Long ownerId);

    List<Item> findAllByRequestId(Long requestId);

    List<Item> findByRequestIdIn(List<Long> itemRequests, Sort created);
}