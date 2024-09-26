package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.List;

@RestController
@RequestMapping("/genres")
public class GenreController {

    private final GenreStorage genreStorage;

    @Autowired
    public GenreController(@Qualifier("GenreDbStorage") GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Genre> getAllGenres() {
        return genreStorage.getAllGenres();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Genre getGenreById(@PathVariable int id) {
        return genreStorage.getGenreById(id)
                .orElseThrow(() -> new RuntimeException("Жанр с id " + id + " не найден"));
    }
}
