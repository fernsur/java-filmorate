package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage, JdbcTemplate jdbcTemplate) {
        this.userStorage = userStorage;
        this.jdbcTemplate = jdbcTemplate;
    }

    public User userById(int id) {
        return userStorage.getById(id);
    }

    public List<User> allUsers() {
        return userStorage.getAllUsers();
    }

    public User createUser(User user) {
        validateUser(user);
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        validateUser(user);
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return userStorage.updateUser(user);
    }

    public void deleteUser(int id) {
        userStorage.deleteUser(id);
    }

    public void addFriend(int userId, int friendId) {
        validationId(userId, friendId);
        String sqlIns = "INSERT INTO FRIENDS (FROM_USER_ID, TO_USER_ID) VALUES (?, ?)";
        jdbcTemplate.update(sqlIns, userId, friendId);
        log.debug("Пользователь " + userId + " добавил в друзья пользователя " + friendId);
    }

    public void deleteFriend(int userId, int friendId) {
        validationId(userId, friendId);
        String sql = "DELETE FROM FRIENDS WHERE FROM_USER_ID = ? AND TO_USER_ID = ?";
        jdbcTemplate.update(sql, userId, friendId);
        log.debug("Пользователь удален из друзей");
    }

    public List<User> getUserFriends(int id) {
        String sql = "SELECT * FROM USERS AS U WHERE U.USER_ID IN " +
                "(SELECT F.TO_USER_ID FROM FRIENDS AS F WHERE F.FROM_USER_ID = ?);";
        return jdbcTemplate.query(sql, this::makeUser, id);
    }

    public List<User> commonFriends(int id, int otherId) {
        validationId(id, otherId);
        String sql = "SELECT * FROM USERS AS U WHERE U.USER_ID IN " +
                "(SELECT F.TO_USER_ID FROM FRIENDS AS F WHERE F.FROM_USER_ID = ?" +
                " INTERSECT " +
                "SELECT FR.TO_USER_ID FROM FRIENDS AS FR WHERE FR.FROM_USER_ID = ?);";
        return jdbcTemplate.query(sql, this::makeUser, id, otherId);
    }

    private void validateUser(User user) {
        if (user.getLogin().contains(" ")) {
            String warning = "Логин не может содержать пробелы.";
            log.warn(warning);
            throw new ValidationException(warning);
        }
    }

    private void validationId(int userId, int friendId) {
        if (userId < 0 || friendId < 0) {
            String warning = "Передан некорректный идентификатор";
            log.warn(warning);
            throw new UserNotFoundException(warning);
        }
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
