package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;

import java.time.LocalDate;

@Data
@Builder
public class User {
    private int id;

    @NotEmpty(message = "Почта не может быть пустой.")
    @Email(message = "Получена некорректная почта.")
    private String email;

    @NotNull(message = "Логин не может быть пустым.")
    @NotBlank(message = "Логин не может быть пустым.")
    private String login;

    private String name;

    @PastOrPresent(message = "Дата рождения не может быть в будущем.")
    private LocalDate birthday;
}
