package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;

import java.time.LocalDate;

import java.util.Map;
import java.util.LinkedHashMap;
import java.util.ArrayList;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final Map<Integer, Film> films = new LinkedHashMap<>();
    private int nextId = 1;
    private static final LocalDate DATE_BIRTHDAY_MOVIE = LocalDate.of(1895,12,28);

    @GetMapping()
    public ArrayList<Film> allFilms() {
        log.info("Текущее количество фильмов: {}", films.size());
        return new ArrayList<>(films.values());
    }

    @PostMapping()
    public Film addFilm(@Valid @RequestBody Film film) throws ValidationException {
        try {
            validateFilm(film);
        } catch (ValidationException exp) {
            log.warn(exp.getMessage());
            throw exp;
        }
        film.setId(nextId++);
        films.put(film.getId(), film);
        log.info("Добавленный фильм: {}",film);
        return film;
    }

    @PutMapping()
    public Film updateFilm(@Valid @RequestBody Film film) throws ValidationException {
        try {
            validateFilm(film);
        } catch (ValidationException exp) {
            log.warn(exp.getMessage());
            throw exp;
        }
        if (!films.containsKey(film.getId())) {
            log.warn("Такого фильма нет.");
            throw new ValidationException("Такого фильма нет.");
        }
        films.put(film.getId(), film);
        log.info("Обновленный фильм: {}",film);
        return film;
    }

    private void validateFilm(Film film) throws ValidationException {
        if (film.getReleaseDate().isBefore(DATE_BIRTHDAY_MOVIE)) {
            throw new ValidationException("Дата релиза должна быть не раньше 28 декабря 1895 года.");
        }
    }
}
