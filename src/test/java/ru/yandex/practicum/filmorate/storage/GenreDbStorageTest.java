package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql({"/schema.sql","/data.sql"})
public class GenreDbStorageTest {

    private final GenreStorage genreStorage;

    @Test
    public void shouldGetGenre() {
        Genre genre = Genre.builder()
                .id(1)
                .name("Комедия")
                .build();

        assertEquals(genre, genreStorage.getGenreById(1));
    }

    @Test
    public void shouldGetAllGenres() {
        List<Genre> genres = new ArrayList<>();
        Genre genre1 = Genre.builder()
                .id(1)
                .name("Комедия")
                .build();
        genres.add(genre1);
        Genre genre2 = Genre.builder()
                .id(2)
                .name("Драма")
                .build();
        genres.add(genre2);
        Genre genre3 = Genre.builder()
                .id(3)
                .name("Мультфильм")
                .build();
        genres.add(genre3);
        Genre genre4 = Genre.builder()
                .id(4)
                .name("Триллер")
                .build();
        genres.add(genre4);
        Genre genre5 = Genre.builder()
                .id(5)
                .name("Документальный")
                .build();
        genres.add(genre5);
        Genre genre6 = Genre.builder()
                .id(6)
                .name("Боевик")
                .build();
        genres.add(genre6);

        assertEquals(genres, genreStorage.getAllGenres());
    }
}
