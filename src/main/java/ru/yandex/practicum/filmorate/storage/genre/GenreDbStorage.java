package ru.yandex.practicum.filmorate.storage.genre;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component()
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Genre getGenreById(Integer id) {
        String sql = "SELECT * FROM GENRES WHERE GENRE_ID = ?";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, id);
        if (!sqlRowSet.next()) {
            String warning = "Невозможно получить. Такого жанра нет.";
            log.warn(warning);
            throw new GenreNotFoundException(warning);
        }
        return Genre.builder()
                .id(sqlRowSet.getInt("GENRE_ID"))
                .name(sqlRowSet.getString("NAME"))
                .build();
    }

    @Override
    public List<Genre> getAllGenres() {
        return jdbcTemplate.query("SELECT * FROM GENRES", this::makeGenre);
    }

    @Override
    public Film writeGenres(Film film) {
        String sqlGenreInsert = "INSERT INTO FILM_GENRE (FILM_ID, GENRE_ID) VALUES (?,?)";
        String sqlGenreSelect = "SELECT NAME FROM GENRES WHERE GENRE_ID = ?";

        if (!film.getGenres().isEmpty()) {
            Collection<Genre> genres = film.getGenres()
                    .stream()
                    .sorted(Comparator.comparing(Genre::getId))
                    .collect(Collectors.toList());
            film.clearGenres();

            for (Genre genre: genres) {
                SqlRowSet sqlRow = jdbcTemplate.queryForRowSet(sqlGenreSelect, genre.getId());
                sqlRow.next();
                String gen = sqlRow.getString("NAME");
                genre.setName(gen);
                film.addGenre(genre);

                jdbcTemplate.update(sqlGenreInsert, film.getId(), genre.getId());
            }
        }
        return film;
    }

    private Genre makeGenre(ResultSet rs, int rowNum) throws SQLException {
        return Genre.builder()
                .id(rs.getInt("GENRE_ID"))
                .name(rs.getString("NAME"))
                .build();
    }
}
