package ru.yandex.practicum.filmorate.storage.like;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component("likeDbStorage")
public class LikeDbStorage implements LikeStorage {
    private final JdbcTemplate jdbcTemplate;

    public LikeDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addLike(int filmId, int userId) {
        String sql = "INSERT INTO FILM_LIKE (FILM_ID, USER_ID) VALUES (?, ?)";
        jdbcTemplate.update(sql, filmId, userId);
        log.debug("Лайк пользователя" + userId + "поставлен фильму " + filmId);
    }

    @Override
    public void deleteLike(int filmId, int userId) {
        String sql = "DELETE FROM FILM_LIKE WHERE FILM_ID = ? AND USER_ID = ?";
        jdbcTemplate.update(sql, filmId, userId);
        log.debug("Лайк пользователя" + userId + "удален у фильма " + filmId);
    }
}
