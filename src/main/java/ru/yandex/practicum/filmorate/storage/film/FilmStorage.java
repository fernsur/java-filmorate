package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    Film getById(Integer id);

    List<Film> getAllFilms();

    Film createFilm(Film film);

    Film updateFilm(Film film);

    void deleteFilm(Integer id);

    void addLike(int filmId, int userId);

    void deleteLike(int filmId, int userId);

    List<Film> popularFilms(int count);
}
