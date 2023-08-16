package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private static final LocalDate DATE_BIRTHDAY_MOVIE = LocalDate.of(1895,12,28);

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage) {
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

    public void addLike(int filmId, int userId) {
        validationId(filmId, userId);
        filmStorage.addLike(filmId, userId);
    }

    public void deleteLike(int filmId, int userId) {
        validationId(filmId, userId);
        filmStorage.deleteLike(filmId, userId);
    }

    public List<Film> popular(int count) {
        return filmStorage.popularFilms(count);
    }

    private void validateFilm(Film film) throws ValidationException {
        if (film.getReleaseDate().isBefore(DATE_BIRTHDAY_MOVIE)) {
            String warning = "Дата релиза должна быть не раньше 28 декабря 1895 года.";
            log.warn(warning);
            throw new ValidationException(warning);
        }
    }

    private void validationId(int filmId, int userId) {
        String warning = "Передан некорректный идентификатор";
        if (filmId < 0) {
            log.warn(warning);
            throw new FilmNotFoundException(warning);
        }
        if (userId < 0) {
            log.warn(warning);
            throw new UserNotFoundException(warning);
        }
    }
}
