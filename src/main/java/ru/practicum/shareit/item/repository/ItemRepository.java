package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
@Component
public interface ItemRepository extends JpaRepository<Item, Long> {
    @Query(value = "select id, name, description, available, owner_id, request_id, number_of_times_to_rent " +
            "from ITEMS " +
            "where owner_id = ?1", nativeQuery = true)
    List<ItemDto> getItemsByUser(Long userId);

    //   List<ItemDto> getItemsByUser(Long userId);
    // List<Item> findByUserId(long userId);

  //  Optional<Item> findByUserIdAndUrl(long userId, String url);


  //  void deleteByUserIdAndId(long userId, long itemId);
}
