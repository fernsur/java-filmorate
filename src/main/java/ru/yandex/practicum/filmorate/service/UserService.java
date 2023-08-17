package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friend.FriendStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;
    private final FriendStorage friendStorage;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage, FriendStorage friendStorage) {
        this.userStorage = userStorage;
        this.friendStorage = friendStorage;
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
        friendStorage.addFriend(userId, friendId);
    }

    public void deleteFriend(int userId, int friendId) {
        validationId(userId, friendId);
        friendStorage.deleteFriend(userId, friendId);
    }

    public List<User> getUserFriends(int id) {
        return userStorage.getUserFriends(id);
    }

    public List<User> commonFriends(int id, int otherId) {
        validationId(id, otherId);
        return userStorage.commonFriends(id, otherId);
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
}
