package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class Film {
    private int id;

    @NotBlank(message = "Название фильма не может быть пустым.")
    private String name;

    @Size(max = 200, message = "Максимальная длина описания не может превышать 200 символов.")
    private String description;

    private LocalDate releaseDate;

    @Positive(message = "Продолжительность фильма не может быть отрицательной.")
    private long duration;

    @JsonIgnore
    private final Set<Integer> likes = new HashSet<>();

    public void addLike(int id) {
        likes.add(id);
    }

    public void deleteLike(int id) {
        if (!likes.contains(id)) {
            throw new UserNotFoundException("Нельзя удалить лайк. Лайка от пользователя не существует.");
        }
        likes.remove(id);
    }

    public int countLike() {
        return likes.size();
    }
}
