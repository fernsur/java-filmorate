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

import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void shouldCreateUserWithCorrectData() throws Exception {
        User user = User.builder()
                .email("test@mail.ru")
                .login("meow")
                .name("Кот")
                .birthday(LocalDate.of(2003,10,11))
                .build();

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void shouldNotCreateUserWithIncorrectEmailSpace() throws Exception {
        User user = User.builder()
                .email(" ")
                .login("meow")
                .name("Кот")
                .birthday(LocalDate.of(2003,10,11))
                .build();

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldNotCreateUserWithIncorrectEmailEmpty() throws Exception {
        User user = User.builder()
                .email("")
                .login("meow")
                .name("Кот")
                .birthday(LocalDate.of(2003,10,11))
                .build();

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldNotCreateUserWithIncorrectEmailNull() throws Exception {
        User user = User.builder()
                .login("meow")
                .name("Кот")
                .birthday(LocalDate.of(2003,10,11))
                .build();

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldNotCreateUserWithIncorrectEmail() throws Exception {
        User user = User.builder()
                .email("meow")
                .login("meow")
                .name("Кот")
                .birthday(LocalDate.of(2003,10,11))
                .build();

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldNotCreateUserWithIncorrectLoginSpace() throws Exception {
        User user = User.builder()
                .email("test@mail.ru")
                .login(" ")
                .name("Кот")
                .birthday(LocalDate.of(2003,10,11))
                .build();

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldNotCreateUserWithIncorrectLoginEmpty() throws Exception {
        User user = User.builder()
                .email("test@mail.ru")
                .login("")
                .name("Кот")
                .birthday(LocalDate.of(2003,10,11))
                .build();

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldNotCreateUserWithIncorrectLoginNull() throws Exception {
        User user = User.builder()
                .email("test@mail.ru")
                .name("Кот")
                .birthday(LocalDate.of(2003,10,11))
                .build();

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldNotCreateUserWithIncorrectBirthday() throws Exception {
        User user = User.builder()
                .email("test@mail.ru")
                .login("meow")
                .name("Кот")
                .birthday(LocalDate.of(2040,10,11))
                .build();

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldUpdateUser() throws Exception {
        User user1 = User.builder()
                .email("test@mail.ru")
                .login("meow")
                .name("Кот")
                .birthday(LocalDate.of(2003,10,11))
                .build();

        mockMvc.perform(post("/users")
                .content(objectMapper.writeValueAsString(user1))
                .contentType(MediaType.APPLICATION_JSON));

        User user2 = User.builder()
                .id(1)
                .email("test@mail.ru")
                .login("meow")
                .name("Кот")
                .birthday(LocalDate.of(2003,10,11))
                .build();

        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user2)))
                .andExpect(status().is2xxSuccessful())
                .andDo(print());
    }

    @Test
    public void shouldReturnAllUsers() throws Exception {
        User user = User.builder()
                .email("test@mail.ru")
                .login("meow")
                .name("Кот")
                .birthday(LocalDate.of(2003,10,11))
                .build();

        mockMvc.perform(post("/users")
                .content(objectMapper.writeValueAsString(user))
                .contentType(MediaType.APPLICATION_JSON));

        mockMvc.perform(get("/users"))
                .andExpect(status().is2xxSuccessful())
                .andDo(print());
    }
}
