package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.mpa.MpaRatingStorage;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/mpa")
public class MpaRatingController {

    private final MpaRatingStorage mpaRatingStorage;

    @Autowired
    public MpaRatingController(@Qualifier("MpaRatingDbStorage") MpaRatingStorage mpaRatingStorage) {
        this.mpaRatingStorage = mpaRatingStorage;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public MpaRating getMpaById(@PathVariable int id) {
        return mpaRatingStorage.getMpaById(id)
                .orElseThrow(() -> new NotFoundException("MPA рейтинг с id " + id + " не найден"));
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<MpaRating> getAllMpa(@RequestParam(defaultValue = "5") int limit) {
        return mpaRatingStorage.getAllMpa().stream()
                .limit(limit)
                .collect(Collectors.toList());
    }
}
