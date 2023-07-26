package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

import java.awt.print.Pageable;
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
    List<Item> findAllByOwnerId(Long userId, Pageable pageable);
    List<Item> findByNameIsContainingIgnoreCaseOrDescriptionIsContainingIgnoreCase(String textInName, String textInDescription);

    @Query(value = "SELECT id FROM ITEMS WHERE owner_id = ?1", nativeQuery = true)
    List<Long> findItemIdsByOwnerId(Long ownerId);

    List<Item> findAllByRequestId(Long requestId);
}