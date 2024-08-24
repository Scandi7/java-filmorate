package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final List<Film> films = new ArrayList<>();
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private final AtomicInteger idCounter = new AtomicInteger(1);

    @PostMapping
    public ResponseEntity<?> addFilm(@Valid @RequestBody Film film) {
        Map<String, String> response = new HashMap<>();
        try {
            if (film.getName() == null || film.getName().isEmpty()) {
                throw new ValidationException("Название фильма не может быть пустым");
            }
            if (film.getDescription() == null || film.getDescription().length() > 200) {
                throw new ValidationException("Описание фильма не может превышать 200 символов");
            }
            if (film.getDuration() <= 0) {
                throw new ValidationException("Продолжительность фильма должна быть положительным числом");
            }

            film.setId(idCounter.getAndIncrement());
            films.add(film);
            log.info("Фильм добавлен: {}", film);
            return new ResponseEntity<>(film, HttpStatus.CREATED);
        } catch (ValidationException ex) {
            log.warn("Ошибка валидации фильма: {}", ex.getMessage());
            response.put("error", ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping
    public ResponseEntity<?> updateFilm(@Valid @RequestBody Film film) {
        Map<String, String> response = new HashMap<>();
        try {
            if (film.getName() == null || film.getName().isEmpty()) {
                throw new ValidationException("Название фильма не может быть пустым");
            }
            if (film.getDescription() == null || film.getDescription().length() > 200) {
                throw new ValidationException("Описание фильма не может превышать 200 символов");
            }
            if (film.getDuration() <= 0) {
                throw new ValidationException("Продолжительность фильма должна быть положительным числом");
            }

            for (int i = 0; i < films.size(); i++) {
                if (films.get(i).getId() == film.getId()) {
                    films.set(i, film);
                    log.info("Фильм обновлен: {}", film);
                    return new ResponseEntity<>(film, HttpStatus.OK);
                }
            }
            log.warn("Фильм с id {} не найден", film.getId());
            response.put("error", "Фильм с таким id не найден");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (ValidationException ex) {
            log.warn("Ошибка валидации фильма: {}", ex.getMessage());
            response.put("error", ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return films;
    }
}
