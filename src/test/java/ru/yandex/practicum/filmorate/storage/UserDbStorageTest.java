package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql({"/schema.sql","/data.sql"})
public class UserDbStorageTest {

    private final UserStorage userStorage;

    @Test
    public void shouldCreateAndGetUser() {
        User user = User.builder()
                .email("test@mail.ru")
                .login("meow")
                .name("Кот")
                .birthday(LocalDate.of(2003,10,11))
                .build();

        User newUser = userStorage.createUser(user);
        assertEquals(newUser, userStorage.getById(1));
    }

    @Test
    public void shouldUpdateUser() {
        User user1 = User.builder()
                .email("test@mail.ru")
                .login("meow")
                .name("Кот")
                .birthday(LocalDate.of(2003,10,11))
                .build();
        userStorage.createUser(user1);

        User user2 = User.builder()
                .id(1)
                .email("test@mail.ru")
                .login("meow")
                .name("Черный кот")
                .birthday(LocalDate.of(2003,10,11))
                .build();
        userStorage.updateUser(user2);

        assertEquals(user2, userStorage.getById(1));
    }

    @Test
    public void shouldReturnAllUsers() {
        List<User> users = new ArrayList<>();

        User user1 = User.builder()
                .email("test@mail.ru")
                .login("meow")
                .name("Кот")
                .birthday(LocalDate.of(2003,10,11))
                .build();
        user1 = userStorage.createUser(user1);
        users.add(user1);

        User user2 = User.builder()
                .email("testtest@mail.ru")
                .login("gaf")
                .name("Собака")
                .birthday(LocalDate.of(2011,11,11))
                .build();
        user2 = userStorage.createUser(user2);
        users.add(user2);

        assertEquals(users.size(), userStorage.getAllUsers().size());
    }

    @Test
    public void shouldDeleteUser() {
        User user = User.builder()
                .email("test@mail.ru")
                .login("meow")
                .name("Кот")
                .birthday(LocalDate.of(2003,10,11))
                .build();
        userStorage.createUser(user);
        userStorage.deleteUser(1);

        assertEquals(0, userStorage.getAllUsers().size());
    }
}
