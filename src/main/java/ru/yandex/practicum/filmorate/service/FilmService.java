package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private static final LocalDate DATE_BIRTHDAY_MOVIE = LocalDate.of(1895,12,28);

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Film filmById(int id) {
        return filmStorage.getById(id);
    }

    public List<Film> allFilms() {
        return filmStorage.getAllFilms();
    }

    public Film createFilm(Film film) {
        validateFilm(film);
        return filmStorage.createFilm(film);
    }

    public Film updateFilm(Film film) {
        validateFilm(film);
        return filmStorage.updateFilm(film);
    }

    public void deleteFilm(int id) {
        filmStorage.deleteFilm(id);
    }

    public Film addLike(int id, int userId) {
        Film film = filmStorage.getById(id);
        film.addLike(userId);
        log.debug("Лайк пользователя" + userId + "поставлен фильму " + id);
        return film;
    }

    public void deleteLike(int id, int userId) {
        Film film = filmStorage.getById(id);
        film.deleteLike(userId);
        log.debug("Лайк пользователя" + userId + "удален у фильма " + id);
    }

    public List<Film> popular(int count) {
        List<Film> popular = filmStorage.getAllFilms()
                .stream()
                .sorted(Comparator.comparing(Film::countLike).reversed())
                .limit(count)
                .collect(Collectors.toList());
        log.debug("Получен список из" + popular.size() + "популярных фильмов.");
        return popular;
    }

    private void validateFilm(Film film) throws ValidationException {
        if (film.getReleaseDate().isBefore(DATE_BIRTHDAY_MOVIE)) {
            String warning = "Дата релиза должна быть не раньше 28 декабря 1895 года.";
            log.warn(warning);
            throw new ValidationException(warning);
        }
    }
}
