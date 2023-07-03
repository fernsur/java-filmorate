package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final Map<Integer,User> users = new HashMap<>();
    private int nextId = 1;

    @GetMapping()
    public List<User> allUsers() {
        log.info("Текущее количество пользователей: {}", users.size());
        return new ArrayList<>(users.values());
    }

    @PostMapping()
    public User createUser(@Valid @RequestBody User user) throws ValidationException {
        validateUser(user);
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        user.setId(nextId++);
        users.put(user.getId(), user);
        log.info("Созданный пользователь: {}",user);
        return user;
    }

    @PutMapping()
    public User updateUser(@Valid @RequestBody User user) throws ValidationException {
        validateUser(user);
        if (!users.containsKey(user.getId())) {
            log.warn("Пользователь с id = {} не существует", user.getId());
            throw new ValidationException("Такого пользователя нет.");
        }
        users.put(user.getId(), user);
        log.info("Обновленный пользователь: {}",user);
        return user;
    }

    private void validateUser(User user) throws ValidationException {
        if (user.getLogin().contains(" ")) {
            String warning = "Логин не может содержать пробелы.";
            log.warn(warning);
            throw new ValidationException(warning);
        }
    }
}
