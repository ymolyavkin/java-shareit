package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemServiceImpl;

import javax.validation.Valid;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemMapper itemMapper;
    private final ItemServiceImpl itemServiceImpl;

    @GetMapping
    public List<Item> getItems() {
        return itemServiceImpl.getItems();
    }

    @PostMapping
    public Item addItem(@Valid @RequestBody Item item) {
        log.info("Получена сущность item");
        return itemServiceImpl.addItem(item);
    }

    @PutMapping
    public Item updateitem(@Valid @RequestBody Item item) {
        log.info("Получена сущность item");
        return itemServiceImpl.updateItem(item);
    }

   /* @GetMapping("/{id}")
    public item getitem(@PathVariable Long id) {
        return itemServiceImpl.getitem(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable Long id, @PathVariable Long userId) {
        itemServiceImpl.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable Long id, @PathVariable Long userId) {
        itemServiceImpl.removeLike(id, userId);
    }

    @GetMapping("/popular")
    public List<item> getMostPopulars(@RequestParam(defaultValue = "10", required = false) @Min(1) Long count,
                                      @RequestParam(required = false) @Min(1) Integer genreId,
                                      @RequestParam(required = false) @Min(1895) String year) {
        return itemServiceImpl.getTopitems(count, genreId, year);
    }

    @GetMapping("/search")
    public List<item> getitemsWithQueryByTitleAndDirector(
            @RequestParam @NotBlank String query,
            @RequestParam List<String> by) {
        return itemServiceImpl.getitemsWithQueryByTitleAndDirector(query, by);
    }

    @GetMapping("/director/{directorId}")
    public List<item> getSorteditemsByDirector(@PathVariable("directorId") Long directorId,
                                               @RequestParam("sortBy") String sort) {
        return itemServiceImpl.getitemsByDirectorSortedBy(directorId, sort);
    }

    @GetMapping("/common")
    public List<item> getCommonitems(@RequestParam(name = "userId") long userId,
                                     @RequestParam(name = "friendId") long friendId) {
        return itemServiceImpl.getCommonitems(userId, friendId);
    }

    @DeleteMapping("{itemId}")
    public void deleteitem(@PathVariable("itemId") long itemId) {
        itemServiceImpl.deleteitemById(itemId);
    }
*/
}
