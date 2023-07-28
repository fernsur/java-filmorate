package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

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

    @JsonIgnore
    private final Set<Integer> friends = new HashSet<>();

    public void addFriend(int id) {
        friends.add(id);
    }

    public void deleteFriend(int id) {
        friends.remove(id);
    }
}
