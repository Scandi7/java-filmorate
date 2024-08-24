package ru.yandex.practicum.filmorate.modelTest;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import javax.validation.Validator;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class FilmTest {
    private final Validator validator;

    public FilmTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void nameIsNull() {
        Film film = new Film();
        film.setName(null);
        film.setDescription("Test description");
        film.setReleaseDate(LocalDate.of(2020,1,1));
        film.setDuration(60);

        assertFalse(validator.validate(film).isEmpty());
    }

    @Test
    void descriptionTooLong() {
        Film film = new Film();
        film.setName("Test");
        film.setDescription("Test description too long Test description too long Test description too long " +
                "Test description too long Test description too long Test description too long" +
                "Test description too long Test description too long Test description too long");
        film.setReleaseDate(LocalDate.of(2020,1,1));
        film.setDuration(60);

        assertFalse(validator.validate(film).isEmpty());
    }

    @Test
    void releaseDateEarly() {
        Film film = new Film();
        film.setName("Test");
        film.setDescription("Test description");
        film.setDuration(60);

        ValidationException exception = assertThrows(
                ValidationException.class, () -> film.setReleaseDate(LocalDate.of(1800, 1, 1)));
        assertEquals("Дата релиза не может быть раньше 28.12.1895", exception.getMessage());
    }

    @Test
    void durationIsNegative() {
        Film film = new Film();
        film.setName("Test");
        film.setDescription("Test description");
        film.setReleaseDate(LocalDate.of(2020,1,1));
        film.setDuration(-60);

        assertFalse(validator.validate(film).isEmpty());
    }
}

