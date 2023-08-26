package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql({"/schema.sql","/data.sql"})
public class LikeDbStorageTest {

    private final LikeStorage likeStorage;

    private final FilmStorage filmStorage;

    private final UserStorage userStorage;

    @Test
    public void shouldAddLikeAndGetPopularFilms() {
        User user1 = User.builder()
                .email("test@mail.ru")
                .login("meow")
                .name("Кот")
                .birthday(LocalDate.of(2003,10,11))
                .build();
        userStorage.createUser(user1);

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
        film.setId(1);

        likeStorage.addLike(1,1);
        assertEquals(List.of(film), filmStorage.popularFilms(1));
    }

    @Test
    public void shouldDeleteLike() {
        User user1 = User.builder()
                .email("test@mail.ru")
                .login("meow")
                .name("Кот")
                .birthday(LocalDate.of(2003,10,11))
                .build();
        userStorage.createUser(user1);

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

        likeStorage.addLike(1,1);
        likeStorage.deleteLike(1,1);
    }
}
