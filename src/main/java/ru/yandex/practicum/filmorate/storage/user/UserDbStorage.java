package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component("userDbStorage")
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User getById(int id) {
        String sql = "SELECT * FROM USERS WHERE USER_ID = ?";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, id);
        if (!sqlRowSet.next()) {
            String warning = "Невозможно получить. Такого пользователя нет.";
            log.warn(warning);
            throw new UserNotFoundException(warning);
        }
        return User.builder()
                .id(sqlRowSet.getInt("USER_ID"))
                .email(sqlRowSet.getString("EMAIL"))
                .login(sqlRowSet.getString("LOGIN"))
                .name(sqlRowSet.getString("NAME"))
                .birthday(sqlRowSet.getDate("BIRTHDAY").toLocalDate())
                .build();
    }

    @Override
    public List<User> getAllUsers() {
        return jdbcTemplate.query("SELECT * FROM USERS", this::makeUser);
    }

    @Override
    public User createUser(User user) {
        String sql = "INSERT INTO USERS (EMAIL, LOGIN, NAME, BIRTHDAY) " +
                "VALUES (?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"USER_ID"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            stmt.setString(4, user.getBirthday().toString());
            return stmt;
        }, keyHolder);
        user.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        return user;
    }

    @Override
    public User updateUser(User user) {
        String sql = "UPDATE USERS SET " +
                "EMAIL = ?, LOGIN = ?, NAME = ?, BIRTHDAY = ? " +
                "WHERE USER_ID = ?";
        if (jdbcTemplate.update(sql, user.getEmail(), user.getLogin(), user.getName(),
                user.getBirthday(), user.getId()) == 0) {
            String warning = "Невозможно обновить. Такого пользователя нет.";
            log.warn(warning);
            throw new UserNotFoundException(warning);
        }
        return user;
    }

    @Override
    public void deleteUser(int id) {
        if (jdbcTemplate.update("DELETE FROM USERS WHERE USER_ID = ?", id) == 0) {
            String warning = "Невозможно удалить. Такого пользователя нет.";
            log.warn(warning);
            throw new UserNotFoundException(warning);
        }
    }

    @Override
    public List<User> getUserFriends(int id) {
        String sql = "SELECT * FROM USERS AS U WHERE U.USER_ID IN " +
                "(SELECT F.FRIEND_ID FROM FRIENDS AS F WHERE F.USER_ID = ?);";
        return jdbcTemplate.query(sql, this::makeUser, id);
    }

    @Override
    public List<User> commonFriends(int id, int otherId) {
        String sql = "SELECT * FROM USERS AS U WHERE U.USER_ID IN " +
                "(SELECT F.FRIEND_ID FROM FRIENDS AS F WHERE F.USER_ID = ?" +
                " INTERSECT " +
                "SELECT FR.FRIEND_ID FROM FRIENDS AS FR WHERE FR.USER_ID = ?);";
        return jdbcTemplate.query(sql, this::makeUser, id, otherId);
    }

    private User makeUser(ResultSet rs, int rowNum) throws SQLException {
        return User.builder()
                .id(rs.getInt("USER_ID"))
                .email(rs.getString("EMAIL"))
                .login(rs.getString("LOGIN"))
                .name(rs.getString("NAME"))
                .birthday(rs.getDate("BIRTHDAY").toLocalDate())
                .build();
    }
}
