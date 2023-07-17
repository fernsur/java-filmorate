package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final FilmService filmService;
    private static final LocalDate DATE_BIRTHDAY_MOVIE = LocalDate.of(1895,12,28);

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping("/{id}")
    public Film filmById(@PathVariable Integer id) {
        log.info("Получен GET-запрос к эндпоинту /films/{id} на получение фильма по id.");
        return filmService.filmById(id);
    }

    @GetMapping()
    public List<Film> allFilms() {
        log.info("Получен GET-запрос к эндпоинту /films на получение всех фильмов.");
        return filmService.allFilms();
    }

    @GetMapping("/popular")
    public List<Film> popular(@RequestParam(name = "count", defaultValue = "10") int count) {
        log.info("Получен GET-запрос к эндпоинту /films/popular на получение " +
                "списка count популярных фильмов по количеству лайков");
        return filmService.popular(count);
    }

    @PostMapping()
    public Film addFilm(@Valid @RequestBody Film film) {
        log.info("Получен POST-запрос к эндпоинту /films на добавление фильма.");
        validateFilm(film);
        return filmService.createFilm(film);
    }

    @PutMapping()
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("Получен PUT-запрос к эндпоинту /films на обновление фильма.");
        validateFilm(film);
        return filmService.updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public Film likeFilm(@PathVariable Integer id,
                         @PathVariable Integer userId) {
        log.info("Получен PUT-запрос к эндпоинту /films/{id}/like/{userId} на добавление лайка конкретному фильму.");
        return filmService.like(id,userId);
    }

    @DeleteMapping("/{id}")
    public void deleteFilm(@PathVariable Integer id) {
        log.info("Получен DELETE-запрос к эндпоинту /films/{id} на удаление фильма.");
        filmService.deleteFilm(id);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLikeFilm(@PathVariable Integer id,
                               @PathVariable Integer userId) {
        log.info("Получен DELETE-запрос к эндпоинту /films/{id}/like/{userId} на удаление лайка у конкретного фильма.");
        filmService.deleteLike(id,userId);
    }

    private void validateFilm(Film film) throws ValidationException {
        if (film.getReleaseDate().isBefore(DATE_BIRTHDAY_MOVIE)) {
            String warning = "Дата релиза должна быть не раньше 28 декабря 1895 года.";
            log.warn(warning);
            throw new ValidationException(warning);
        }
    }
}
