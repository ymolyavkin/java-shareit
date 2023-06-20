package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;



@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemMapper itemMapper;

    @GetMapping
    public List<Film> getFilms() {
        return filmService.getFilms();
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        log.info("Получена сущность Film");
        return filmService.addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("Получена сущность Film");
        return filmService.updateFilm(film);
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable Long id) {
        return filmService.getFilm(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable Long id, @PathVariable Long userId) {
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable Long id, @PathVariable Long userId) {
        filmService.removeLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getMostPopulars(@RequestParam(defaultValue = "10", required = false) @Min(1) Long count,
                                      @RequestParam(required = false) @Min(1) Integer genreId,
                                      @RequestParam(required = false) @Min(1895) String year) {
        return filmService.getTopFilms(count, genreId, year);
    }

    @GetMapping("/search")
    public List<Film> getFilmsWithQueryByTitleAndDirector(
            @RequestParam @NotBlank String query,
            @RequestParam List<String> by) {
        return filmService.getFilmsWithQueryByTitleAndDirector(query, by);
    }

    @GetMapping("/director/{directorId}")
    public List<Film> getSortedFilmsByDirector(@PathVariable("directorId") Long directorId,
                                               @RequestParam("sortBy") String sort) {
        return filmService.getFilmsByDirectorSortedBy(directorId, sort);
    }

    @GetMapping("/common")
    public List<Film> getCommonFilms(@RequestParam(name = "userId") long userId,
                                     @RequestParam(name = "friendId") long friendId) {
        return filmService.getCommonFilms(userId, friendId);
    }

    @DeleteMapping("{filmId}")
    public void deleteFilm(@PathVariable("filmId") long filmId) {
        filmService.deleteFilmById(filmId);
    }

}
