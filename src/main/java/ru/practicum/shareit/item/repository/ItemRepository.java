package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Component
public interface ItemRepository extends JpaRepository<Item, Long> {
    @Query(value = "select * " +
            "from ITEMS " +
            "where owner_id = ?1", nativeQuery = true)
//@Query("SELECT i FROM ITEMS i WHERE i.owner_id = ?1")
//List<Tutorial> findByPublished();
    List<Item> findAllByOwnerId(Long userId);

    //   List<ItemDto> getItemsByUser(Long userId);
    // List<Item> findByUserId(long userId);

  //  Optional<Item> findByUserIdAndUrl(long userId, String url);


  //  void deleteByUserIdAndId(long userId, long itemId);
}
