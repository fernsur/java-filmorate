package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

@SpringBootTest
public class FilmControllerTest {

    private FilmController filmController;

    @BeforeEach
    public void initFilmController() {
        filmController = new FilmController();
    }

    @Test
    public void shouldReturnFilmWithCorrectData() throws ValidationException {
        Film film = Film.builder()
                .name("Test")
                .description("Test description")
                .releaseDate(LocalDate.of(2003,10,11))
                .duration(124)
                .build();

        Film newFilm = filmController.addFilm(film);

        assertEquals(film,newFilm);
    }

    @Test
    public void shouldExceptionIfFilmWithEmptyName() throws ValidationException {
        Film film = Film.builder()
                .name("")
                .description("Test description")
                .releaseDate(LocalDate.of(2003,10,11))
                .duration(124)
                .build();

        Film newFilm = filmController.addFilm(film);

        assertEquals(film,newFilm);
    }
}
