package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;

import java.sql.Date;
import java.sql.PreparedStatement;

import java.util.Collection;
import java.util.Comparator;
import java.util.Objects;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Slf4j
@Component("filmDbStorage")
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final GenreDbStorage genreDbStorage;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate, GenreDbStorage genreDbStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreDbStorage = genreDbStorage;
    }

    @Override
    public Film getById(Integer id) {
        String sql = "SELECT F.*, R.NAME AS MPA_NAME FROM FILMS AS F " +
                "INNER JOIN MPA_RATING AS R ON F.MPA_RATING_ID = R.MPA_RATING_ID " +
                "WHERE FILM_ID = ?";

        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, id);
        if (!sqlRowSet.next()) {
            String warning = "Невозможно получить. Такого фильма нет.";
            log.warn(warning);
            throw new FilmNotFoundException(warning);
        }

        Mpa mpa = Mpa.builder()
                .id(sqlRowSet.getInt("MPA_RATING_ID"))
                .name(sqlRowSet.getString("MPA_NAME"))
                .build();

        Film film = Film.builder()
                .id(sqlRowSet.getInt("FILM_ID"))
                .name(sqlRowSet.getString("NAME"))
                .description(sqlRowSet.getString("DESCRIPTION"))
                .releaseDate(sqlRowSet.getDate("RELEASE_DATE").toLocalDate())
                .duration(sqlRowSet.getInt("DURATION"))
                .mpa(mpa)
                .build();

        String sqlGenre = "SELECT GENRE_ID FROM FILM_GENRE WHERE FILM_ID = ?";
        SqlRowSet sqlRow = jdbcTemplate.queryForRowSet(sqlGenre, id);
        while (sqlRow.next()) {
            int idG = sqlRow.getInt("GENRE_ID");
            film.addGenre(genreDbStorage.getGenreById(idG));
        }

        return film;
    }

    @Override
    public List<Film> getAllFilms() {
        String sql = "SELECT FILM_ID FROM FILMS ORDER BY FILM_ID";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql);
        List<Film> films = new ArrayList<>();
        while (sqlRowSet.next()) {
            int id = sqlRowSet.getInt("FILM_ID");
            films.add(getById(id));
        }
        return films;
    }

    @Override
    public Film createFilm(Film film) {
        String sqlFilm = "INSERT INTO FILMS (NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA_RATING_ID) " +
                "VALUES (?,?,?,?,?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlFilm, new String[]{"FILM_ID"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            stmt.setInt(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        film.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());

        writeMpa(film);
        writeGenres(film);

        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        String sqlFilm = "UPDATE FILMS SET " +
                "NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ?, MPA_RATING_ID = ? " +
                "WHERE FILM_ID = ?";
        String sqlGenreDelete = "DELETE FROM FILM_GENRE WHERE FILM_ID = ?";

        if (jdbcTemplate.update(sqlFilm, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getMpa().getId(), film.getId()) == 0) {
            String warning = "Невозможно обновить. Такого фильма нет.";
            log.warn(warning);
            throw new FilmNotFoundException(warning);
        }

        writeMpa(film);

        jdbcTemplate.update(sqlGenreDelete, film.getId());
        writeGenres(film);

        return film;
    }

    @Override
    public void deleteFilm(Integer id) {
        if (jdbcTemplate.update("DELETE FROM FILMS WHERE FILM_ID = ?", id) == 0) {
            String warning = "Невозможно удалить. Такого фильма нет.";
            log.warn(warning);
            throw new FilmNotFoundException(warning);
        }
    }

    private Film writeGenres(Film film) {
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

    private Film writeMpa(Film film) {
        String sqlMpa = "SELECT NAME FROM MPA_RATING WHERE MPA_RATING_ID = ?";

        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sqlMpa, film.getMpa().getId());
        sqlRowSet.next();
        String mpa = sqlRowSet.getString("NAME");
        film.getMpa().setName(mpa);

        return film;
    }
}
