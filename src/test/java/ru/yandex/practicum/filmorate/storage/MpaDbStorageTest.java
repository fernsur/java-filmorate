package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MpaDbStorageTest {

    private final MpaStorage mpaStorage;

    @Test
    @Sql({"/schema.sql","/data.sql"})
    public void shouldGetMpa() {
        Mpa mpa = Mpa.builder()
                .id(1)
                .name("G")
                .build();

        assertEquals(mpa, mpaStorage.getMpaById(1));
    }

    @Test
    @Sql({"/schema.sql","/data.sql"})
    public void shouldGetAllMpa() {
        List<Mpa> mpaAll = new ArrayList<>();
        Mpa mpa1 = Mpa.builder()
                .id(1)
                .name("G")
                .build();
        mpaAll.add(mpa1);
        Mpa mpa2 = Mpa.builder()
                .id(2)
                .name("PG")
                .build();
        mpaAll.add(mpa2);
        Mpa mpa3 = Mpa.builder()
                .id(3)
                .name("PG-13")
                .build();
        mpaAll.add(mpa3);
        Mpa mpa4 = Mpa.builder()
                .id(4)
                .name("R")
                .build();
        mpaAll.add(mpa4);
        Mpa mpa5 = Mpa.builder()
                .id(5)
                .name("NC-17")
                .build();
        mpaAll.add(mpa5);

        assertEquals(mpaAll, mpaStorage.getAllMpa());
    }
}
