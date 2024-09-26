package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.mpa.MpaRatingStorage;

import java.util.List;

@RestController
@RequestMapping("/mpa")
public class MpaRatingController {

    private final MpaRatingStorage mpaRatingStorage;

    @Autowired
    public MpaRatingController(@Qualifier("MpaRatingDbStorage") MpaRatingStorage mpaRatingStorage) {
        this.mpaRatingStorage = mpaRatingStorage;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<MpaRating> getAllMpa() {
        return mpaRatingStorage.getAllMpa();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public MpaRating getMpaById(@PathVariable int id) {
        return mpaRatingStorage.getMpaById(id)
                .orElseThrow(() -> new RuntimeException("MPA рейтинг с id " + id + " не найден"));
    }
}
