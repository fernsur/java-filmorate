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
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.sql.Date;
import java.sql.PreparedStatement;

import java.util.Objects;
import java.util.List;
import java.util.ArrayList;

@Slf4j
@Component("filmDbStorage")
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final GenreStorage genreStorage;
    private final MpaStorage mpaStorage;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate, GenreStorage genreStorage, MpaStorage mpaStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreStorage = genreStorage;
        this.mpaStorage = mpaStorage;
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
            film.addGenre(genreStorage.getGenreById(idG));
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

        mpaStorage.writeMpa(film);
        genreStorage.writeGenres(film);

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

        mpaStorage.writeMpa(film);

        jdbcTemplate.update(sqlGenreDelete, film.getId());
        genreStorage.writeGenres(film);

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

    @Override
    public List<Film> popularFilms(int count) {
        String sql = "SELECT F.FILM_ID FROM FILMS AS F " +
                "LEFT JOIN FILM_LIKE AS FL ON F.FILM_ID = FL.FILM_ID " +
                "GROUP BY F.FILM_ID ORDER BY COUNT(FL.USER_ID) DESC LIMIT ?";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, count);
        List<Film> popularFilms = new ArrayList<>();
        while (sqlRowSet.next()) {
            int id = sqlRowSet.getInt("FILM_ID");
            popularFilms.add(getById(id));
        }
        return popularFilms;
    }
}
