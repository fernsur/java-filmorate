package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql({"/schema.sql","/data.sql"})
public class FilmDbStorageTest {

    private final FilmStorage filmStorage;

    @Test
    public void shouldCreateAndGetFilm() {
        Film film = Film.builder()
                .name("Film")
                .description("Test description")
                .releaseDate(LocalDate.of(2003,10,11))
                .duration(124)
                .mpa(Mpa.builder()
                        .id(1)
                        .name("Комедия")
                        .build())
                .build();

        Film newfilm = filmStorage.createFilm(film);
        assertEquals(newfilm, filmStorage.getById(1));
    }

    @Test
    public void shouldUpdateFilm() {
        Film film1 = Film.builder()
                .name("Film")
                .description("Test description")
                .releaseDate(LocalDate.of(2003,10,11))
                .duration(124)
                .mpa(Mpa.builder()
                        .id(1)
                        .name("Комедия")
                        .build())
                .build();
        filmStorage.createFilm(film1);

        Film film2 = Film.builder()
                .id(1)
                .name("Film Update")
                .description("Test description")
                .releaseDate(LocalDate.of(2003,10,11))
                .duration(60)
                .mpa(Mpa.builder()
                        .id(2)
                        .name("Драма")
                        .build())
                .build();
        filmStorage.updateFilm(film2);

        assertEquals(film2, filmStorage.getById(1));
    }

    @Test
    public void shouldReturnAllUsers() {
        List<Film> films = new ArrayList<>();

        Film film1 = Film.builder()
                .name("Film")
                .description("Test description")
                .releaseDate(LocalDate.of(2003,10,11))
                .duration(124)
                .mpa(Mpa.builder()
                        .id(1)
                        .name("Комедия")
                        .build())
                .build();
        film1 = filmStorage.createFilm(film1);
        films.add(film1);

        Film film2 = Film.builder()
                .name("Film 2")
                .description("Test description 2")
                .releaseDate(LocalDate.of(2013,10,11))
                .duration(60)
                .mpa(Mpa.builder()
                        .id(2)
                        .name("Драма")
                        .build())
                .build();
        film2 = filmStorage.createFilm(film2);
        films.add(film2);

        assertEquals(films.size(), filmStorage.getAllFilms().size());
    }

    @Test
    public void shouldDeleteUser() {
        Film film = Film.builder()
                .name("Film")
                .description("Test description")
                .releaseDate(LocalDate.of(2003,10,11))
                .duration(124)
                .mpa(Mpa.builder()
                        .id(1)
                        .name("Комедия")
                        .build())
                .build();

        filmStorage.createFilm(film);
        filmStorage.deleteFilm(1);

        assertEquals(0, filmStorage.getAllFilms().size());
    }
}
