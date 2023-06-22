package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.NoneXSharerUserIdException;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemMapper itemMapper;
    private final ItemService itemService;

    @GetMapping
    public List<Item> getItems() {
        return itemService.getItems();
    }

    @PostMapping
    public Item addItem(@RequestHeader Map<String, String> headers, @Valid @RequestBody Item item) {
        if (headers.containsKey("x-sharer-user-id")) {
            item.setOwner(headers.get("x-sharer-user-id"));
        } else {
            throw new NoneXSharerUserIdException("Не указан владелец вещи");
        }
        return itemService.addItem(item);
    }

    @PatchMapping(value = "/{id}", consumes = "application/json")
    public Item updateItem(@Valid @RequestBody Item item,
                           @PathVariable Long id) {
        return itemService.updateItem(item, id);
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
