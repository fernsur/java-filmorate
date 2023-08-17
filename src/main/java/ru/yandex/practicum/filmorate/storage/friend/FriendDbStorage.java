package ru.yandex.practicum.filmorate.storage.friend;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.StatusFriend;

@Slf4j
@Component("friendDbStorage")
public class FriendDbStorage implements FriendStorage {

    private final JdbcTemplate jdbcTemplate;

    public FriendDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addFriend(int userId, int friendId) {
        String sqlUpd = "UPDATE FRIENDS SET STATUS_FRIEND = ? " +
                "WHERE USER_ID = ? AND FRIEND_ID = ?";
        String sqlIns = "INSERT INTO FRIENDS (USER_ID, FRIEND_ID, STATUS_FRIEND) VALUES (?, ?, ?)";
        String status = StatusFriend.NOT_MUTUAL.toString();

        if (jdbcTemplate.update(sqlUpd, StatusFriend.MUTUAL.toString(), friendId, userId) != 0) {
            log.debug("Пользователь " + userId + " добавил в друзья пользователя " + friendId);
            status = StatusFriend.MUTUAL.toString();
        }

        jdbcTemplate.update(sqlIns, userId, friendId, status);
        log.debug("Пользователь " + userId + " добавил в друзья пользователя " + friendId);
    }

    @Override
    public void deleteFriend(int userId, int friendId) {
        String sql = "DELETE FROM FRIENDS WHERE USER_ID = ? AND FRIEND_ID = ?";
        String sqlUpd = "UPDATE FRIENDS SET STATUS_FRIEND = ? " +
                "WHERE USER_ID = ? AND FRIEND_ID = ?";
        String status = StatusFriend.NOT_MUTUAL.toString();

        if (jdbcTemplate.update(sqlUpd, status, friendId, userId) != 0) {
            log.debug("Пользователь " + userId + " удалил из друзей " + friendId);
        }
        jdbcTemplate.update(sql, userId, friendId);
        log.debug("Пользователь удален из друзей");
    }
}
