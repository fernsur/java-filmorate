package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;

@RestController
@RequestMapping("/genres")
@Slf4j
public class GenreController {
    private final GenreService genreService;

    @Autowired
    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping("/{id}")
    public Genre genreById(@PathVariable int id) {
        log.info("Получен GET-запрос к эндпоинту /genres/{id} на получение жанра по id.");
        return genreService.genreById(id);
    }

    @GetMapping()
    public List<Genre> allGenres() {
        log.info("Получен GET-запрос к эндпоинту /genres на получение всех жанров.");
        return genreService.allGenres();
    }
}
