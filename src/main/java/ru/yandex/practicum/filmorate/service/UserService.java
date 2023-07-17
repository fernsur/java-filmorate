package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User userById(Integer id) {
        return userStorage.getById(id);
    }

    public List<User> allUsers() {
        return userStorage.getAllUsers();
    }

    public User createUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return userStorage.updateUser(user);
    }

    public void deleteUser(Integer id) {
        userStorage.deleteUser(id);
    }

    public User addFriend(Integer id, Integer friendId) {
        User user1 = userStorage.getById(id);
        User user2 = userStorage.getById(friendId);
        user1.addFriend(friendId);
        user2.addFriend(id);
        log.debug("Друг добавлен.");
        return user1;
    }

    public void deleteFriend(Integer id, Integer friendId) {
        User user1 = userStorage.getById(id);
        User user2 = userStorage.getById(friendId);
        user1.deleteFriend(friendId);
        user2.deleteFriend(id);
        log.debug("Друг удален.");
    }

    public List<User> friends(Integer id) {
        User user = userStorage.getById(id);
        List<User> friends = user.getFriends()
                .stream()
                .map(userStorage::getById)
                .collect(Collectors.toList());
        log.debug("Список друзей получен.");
        return friends;
    }

    public List<User> commonFriends(Integer id, Integer otherId) {
        Set<Integer> friendsUser1 = userStorage.getById(id).getFriends();
        Set<Integer> friendsUser2 = userStorage.getById(otherId).getFriends();

        Set<Integer> commonId = new HashSet<>(friendsUser1);
        commonId.retainAll(friendsUser2);

        List<User> friends = commonId
                .stream()
                .map(userStorage::getById)
                .collect(Collectors.toList());
        log.debug("Получен список из" + friends.size() + "общих друзей.");
        return friends;
    }
}
