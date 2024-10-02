package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public abstract class InMemoryFilmStorage implements FilmStorage {

    private final List<Film> films = new ArrayList<>();
    private final AtomicInteger idCounter = new AtomicInteger(1);

    @Override
    public Film addFilm(Film film) {
        film.setId(idCounter.getAndIncrement());
        films.add(film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        for (int i = 0; i < films.size(); i++) {
            if (films.get(i).getId() == film.getId()) {
                films.set(i, film);
                return film;
            }
        }
        return null;
    }

    @Override
    public Optional<Film> getFilmById(int id) {
        return films.stream().filter(film -> film.getId() == id).findFirst();
    }

    @Override
    public List<Film> getAllFilms() {
        return films;
    }
}
