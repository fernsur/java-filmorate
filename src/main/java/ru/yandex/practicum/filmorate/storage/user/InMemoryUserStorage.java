package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private int nextId = 1;

    @Override
    public User getById(Integer id) {
        if (!users.containsKey(id)) {
            String warning = "Невозможно получить. Такого пользователя нет.";
            log.warn(warning);
            throw new UserNotFoundException(warning);
        }
        return users.get(id);
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User createUser(User user) {
        user.setId(nextId++);
        users.put(user.getId(),user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        Integer id = user.getId();
        if (!users.containsKey(id)) {
            log.warn("Пользователь с id = {} не существует", user.getId());
            throw new UserNotFoundException("Такого пользователя нет.");
        }
        users.put(id, user);
        return user;
    }

    @Override
    public void deleteUser(Integer id) {
        if (!users.containsKey(id)) {
            String warning = "Невозможно удалить. Такого пользователя нет.";
            log.warn(warning);
            throw new UserNotFoundException(warning);
        }
        for (User user: users.values()) {
            user.deleteFriend(id);
        }
        users.remove(id);
    }
}
