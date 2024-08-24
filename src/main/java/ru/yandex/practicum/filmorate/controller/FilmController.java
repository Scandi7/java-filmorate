package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final List<Film> films = new ArrayList<>();
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        film.setId(Integer.parseInt(UUID.randomUUID().toString()));
        films.add(film);
        log.info("Фильм добавлен: {}", film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        for (int i = 0; i < films.size(); i++) {
            if (films.get(i).getId() == film.getId()) {
                films.set(i, film);
                log.info("Фильм обновлен: {}", film);
                return film;
            }
        }
        log.warn("Фильм с id {} не найден", film.getId());
        throw new ValidationException("Фильм с таким id не найден");
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return films;
    }
}
