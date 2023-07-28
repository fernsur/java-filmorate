package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    User getById(Integer id);

    List<User> getAllUsers();

    User createUser(User user);

    User updateUser(User user);

    void deleteUser(Integer id);
}
