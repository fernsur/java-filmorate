package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;

@RestController
@RequestMapping("/mpa")
@Slf4j
public class MpaController {
    private final MpaService mpaService;

    @Autowired
    public MpaController(MpaService mpaService) {
        this.mpaService = mpaService;
    }

    @GetMapping("/{id}")
    public Mpa mpaById(@PathVariable int id) {
        log.info("Получен GET-запрос к эндпоинту /mpa/{id} на получение возрастного рейтинга по id.");
        return mpaService.mpaById(id);
    }

    @GetMapping()
    public List<Mpa> allMpa() {
        log.info("Получен GET-запрос к эндпоинту /mpa на получение всех возрастных рейтингов.");
        return mpaService.allMpa();
    }
}
