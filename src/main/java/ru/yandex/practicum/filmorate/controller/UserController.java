package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public User userById(@PathVariable int id) {
        log.info("Получен GET-запрос к эндпоинту /users/{id} на получение пользователя по id.");
        return userService.userById(id);
    }

    @GetMapping()
    public List<User> allUsers() {
        log.info("Получен GET-запрос к эндпоинту /users на получение всех пользователей.");
        return userService.allUsers();
    }

    @GetMapping("/{id}/friends")
    public List<User> getUserFriends(@PathVariable int id) {
        log.info("Получен GET-запрос к эндпоинту /users/{id}/friends на получение друзей конкретного пользователя");
        return userService.getUserFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable int id,
                                       @PathVariable int otherId) {
        log.info("Получен GET-запрос к эндпоинту /users/{id}/friends/common/{otherId} на получение общих друзей.");
        return userService.commonFriends(id,otherId);
    }

    @PostMapping()
    public User addUser(@Valid @RequestBody User user) {
        log.info("Получен POST-запрос к эндпоинту /users на добавление пользователя");
        return userService.createUser(user);
    }

    @PutMapping()
    public User updateUser(@Valid @RequestBody User user) {
        log.info("Получен PUT-запрос к эндпоинту /users на обновление пользователя");
        return userService.updateUser(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable int id,
                          @PathVariable int friendId) {
        log.info("Получен PUT-запрос к эндпоинту /users/{id}/friends/{friendId} на добавление в друзья.");
        userService.addFriend(id,friendId);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable int id) {
        log.info("Получен DELETE-запрос к эндпоинту /users/{id} на удаление пользователя.");
        userService.deleteUser(id);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable int id,
                             @PathVariable int friendId) {
        log.info("Получен DELETE-запрос к эндпоинту /users/{id}/friends/{friendId} на удаление из друзей.");
        userService.deleteFriend(id,friendId);
    }
}
