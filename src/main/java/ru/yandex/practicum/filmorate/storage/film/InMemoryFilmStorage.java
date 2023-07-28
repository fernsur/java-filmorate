package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private Integer nextId = 1;

    @Override
    public Film getById(Integer id) {
        if (!films.containsKey(id)) {
            String warning = "Невозможно получить. Такого фильма нет.";
            log.warn(warning);
            throw new FilmNotFoundException(warning);
        }
        return films.get(id);
    }

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film createFilm(Film film) {
        film.setId(nextId++);
        films.put(film.getId(),film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        Integer id = film.getId();
        if (!films.containsKey(id)) {
            String warning = "Невозможно обновить. Такого фильма нет.";
            log.warn(warning);
            throw new FilmNotFoundException(warning);
        }
        films.put(id,film);
        return film;
    }

    @Override
    public void deleteFilm(Integer id) {
        if (!films.containsKey(id)) {
            String warning = "Невозможно удалить. Такого фильма нет.";
            log.warn(warning);
            throw new FilmNotFoundException(warning);
        }
        films.remove(id);
    }
}
