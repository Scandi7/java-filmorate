package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;
    private final UserService userService;

    @Autowired
    public FilmController(FilmService filmService, UserService userService) {
        this.filmService = filmService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<?> addFilm(@Valid @RequestBody Film film) {
        try {
            Film createdFilm = filmService.addFilm(film);
            return new ResponseEntity<>(createdFilm, HttpStatus.CREATED);
        } catch (ValidationException ex) {
            Map<String, String> response = new HashMap<>();
            response.put("error", ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Внутренняя ошибка сервера");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping
    public ResponseEntity<?> updateFilm(@Valid @RequestBody Film film) {
        Map<String, String> response = new HashMap<>();
        try {
            Film updatedFilm = filmService.updateFilm(film);
            return new ResponseEntity<>(updatedFilm, HttpStatus.OK);
        } catch (ValidationException ex) {
            response.put("error", ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (UserNotFoundException ex) {
            response.put("error", ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return filmService.getAllFilms();
    }

    @PutMapping("/{id}/like/{userId}")
    public ResponseEntity<Map<String, String>> addLike(@PathVariable int id, @PathVariable int userId) {
        Map<String, String> response = new HashMap<>();
        try {
            Optional<Film> filmOpt = filmService.getFilmById(id);
            if (filmOpt.isEmpty()) {
                response.put("error", "Фильм не найден");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            if (!userService.existsById(userId)) {
                response.put("error", "Пользователь не найден");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            filmService.addLike(id, userId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            response.put("error", "Внутренняя ошибка сервера");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}/like/{userId}")
    public ResponseEntity<Map<String, String>> removeLike(@PathVariable int id, @PathVariable int userId) {
        Map<String, String> response = new HashMap<>();
        try {
            Optional<Film> filmOpt = filmService.getFilmById(id);
            if (filmOpt.isEmpty()) {
                response.put("error", "Фильм не найден");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            if (!userService.existsById(userId)) {
                response.put("error", "Пользователь не найден");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            filmService.removeLike(id, userId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            response.put("error", "Внутренняя ошибка сервера");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/popular")
    public ResponseEntity<List<Film>> getPopularFilms(@RequestParam(defaultValue = "10") int count) {
        return new ResponseEntity<>(filmService.getPopularFilms(count), HttpStatus.OK);
    }
}
