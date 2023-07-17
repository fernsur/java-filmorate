package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Film filmById(Integer id) {
        return filmStorage.getById(id);
    }

    public List<Film> allFilms() {
        return filmStorage.getAllFilms();
    }

    public Film createFilm(Film film) {
        return filmStorage.createFilm(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public void deleteFilm(Integer id) {
        filmStorage.deleteFilm(id);
    }

    public Film like(Integer id, Integer userId) {
        Film film = filmStorage.getById(id);
        film.addLike(userId);
        log.debug("Лайк пользователя поставлен фильму.");
        return film;
    }

    public void deleteLike(Integer id, Integer userId) {
        Film film = filmStorage.getById(id);
        film.deleteLike(userId);
        log.debug("Лайк пользователя удален у фильма.");
    }

    public List<Film> popular(Integer count) {
        List<Film> popular = filmStorage.getAllFilms()
                .stream()
                .sorted(Comparator.comparing(Film::countLike).reversed())
                .limit(count)
                .collect(Collectors.toList());
        log.debug("Получен список из" + popular.size() + "популярных фильмов.");
        return popular;
    }
}
