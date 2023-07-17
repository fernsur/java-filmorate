package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

@SpringBootTest
@AutoConfigureMockMvc
public class FilmControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void shouldCreateFilmWithCorrectData() throws Exception {
        Film film = Film.builder()
                .name("Film")
                .description("Test description")
                .releaseDate(LocalDate.of(2003,10,11))
                .duration(124)
                .build();

        mockMvc.perform(post("/films")
                        .content(objectMapper.writeValueAsString(film))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void shouldNotCreateFilmWithIncorrectNameEmpty() throws Exception {
        Film film = Film.builder()
                .name("")
                .description("Test description")
                .releaseDate(LocalDate.of(2003,10,11))
                .duration(124)
                .build();

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldNotCreateFilmWithIncorrectNameNull() throws Exception {
        Film film = Film.builder()
                .description("Test description")
                .releaseDate(LocalDate.of(2003,10,11))
                .duration(124)
                .build();

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldNotCreateFilmWithIncorrectNameSpace() throws Exception {
        Film film = Film.builder()
                .name(" ")
                .description("Test description")
                .releaseDate(LocalDate.of(2003,10,11))
                .duration(124)
                .build();

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldNotCreateFilmWithIncorrectDescription() throws Exception {
        Film film = Film.builder()
                .name("Film")
                .description("Test description description description description description description" +
                        "description description description description description description description" +
                        "description description description description description description")
                .releaseDate(LocalDate.of(2003,10,11))
                .duration(124)
                .build();

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldNotCreateFilmWithIncorrectDuration() throws Exception {
        Film film = Film.builder()
                .name("Film")
                .description("Test description")
                .releaseDate(LocalDate.of(2003,10,11))
                .duration(-124)
                .build();

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldUpdateFilm() throws Exception {
        Film film1 = Film.builder()
                .name("Film")
                .description("Test description")
                .releaseDate(LocalDate.of(2003,10,11))
                .duration(124)
                .build();

        mockMvc.perform(post("/films")
                .content(objectMapper.writeValueAsString(film1))
                .contentType(MediaType.APPLICATION_JSON));

        Film film2 = Film.builder()
                .id(1)
                .name("Film Film")
                .description("Test description Film")
                .releaseDate(LocalDate.of(2003,10,11))
                .duration(124)
                .build();

        mockMvc.perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film2)))
                .andExpect(status().is2xxSuccessful())
                .andDo(print());
    }

    @Test
    public void shouldReturnAllFilms() throws Exception {
        Film film = Film.builder()
                .name("Film")
                .description("Test description")
                .releaseDate(LocalDate.of(2003,10,11))
                .duration(124)
                .build();

        mockMvc.perform(post("/films")
                .content(objectMapper.writeValueAsString(film))
                .contentType(MediaType.APPLICATION_JSON));

        mockMvc.perform(get("/films"))
                .andExpect(status().is2xxSuccessful())
                .andDo(print());
    }
}
