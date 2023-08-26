package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friend.FriendStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql({"/schema.sql","/data.sql"})
public class FriendDbStorageTest {

    private final FriendStorage friendStorage;

    private final UserStorage userStorage;

    @Test
    public void shouldAddFriendAndGetUserFriends() {
        User user1 = User.builder()
                .email("test@mail.ru")
                .login("meow")
                .name("Кот")
                .birthday(LocalDate.of(2003,10,11))
                .build();
        userStorage.createUser(user1);

        User user2 = User.builder()
                .email("test@mail.ru")
                .login("gaf")
                .name("Собака")
                .birthday(LocalDate.of(2013,11,11))
                .build();
        userStorage.createUser(user2);

        friendStorage.addFriend(1,2);
        assertEquals(1,userStorage.getUserFriends(1).size());

        user2.setId(2);
        assertEquals(List.of(user2),userStorage.getUserFriends(1));
    }

    @Test
    public void shouldDeleteFriend() {
        User user1 = User.builder()
                .email("test@mail.ru")
                .login("meow")
                .name("Кот")
                .birthday(LocalDate.of(2003,10,11))
                .build();
        userStorage.createUser(user1);

        User user2 = User.builder()
                .email("test@mail.ru")
                .login("gaf")
                .name("Собака")
                .birthday(LocalDate.of(2013,11,11))
                .build();
        userStorage.createUser(user2);

        friendStorage.addFriend(1,2);
        friendStorage.deleteFriend(1,2);
        assertEquals(0,userStorage.getUserFriends(1).size());
    }
}
