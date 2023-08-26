package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
    private int duration;

    @NotNull(message = "Рейтинг не может быть пустым")
    private Mpa mpa;

    private final Set<Genre> genres = new HashSet<>();

    public void addGenre(Genre genre) {
        genres.add(genre);
    }

    public void clearGenres() {
        genres.clear();
    }
}
