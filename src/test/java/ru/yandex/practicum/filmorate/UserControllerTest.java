package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserControllerTest {
    private UserController userController;

    @BeforeEach
    public void initUserController() {
        userController = new UserController();
    }

    @Test
    public void shouldReturnUserWithCorrectData() throws ValidationException {
        User user = User.builder()
                .email("test@mail.ru")
                .login("meow")
                .name("Кот")
                .birthday(LocalDate.of(2003,10,11))
                .build();

        User newUser = userController.createUser(user);

        assertEquals(user,newUser);
    }
}
