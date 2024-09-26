package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaRatingStorage;

import java.util.List;

import static ru.yandex.practicum.filmorate.validation.ValidateFilm.validateFilm;

@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserService userService;
    private final GenreStorage genreStorage;
    private final MpaRatingStorage mpaRatingStorage;

    @Autowired
    public FilmService(@Qualifier("FilmDbStorage") FilmStorage filmStorage,
                       UserService userService,
                       @Qualifier("GenreDbStorage") GenreStorage genreStorage,
                       @Qualifier("MpaRatingDbStorage") MpaRatingStorage mpaRatingStorage) {
        this.filmStorage = filmStorage;
        this.userService = userService;
        this.genreStorage = genreStorage;
        this.mpaRatingStorage = mpaRatingStorage;
    }

/*    public Film addFilm(Film film) {
        validateFilmWithDependencies(film);
        return filmStorage.addFilm(film);
    }*/

    public Film addFilm(Film film) {
        try {
            validateFilmWithDependencies(film);
        } catch (ValidationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        validateFilmWithDependencies(film);
        filmStorage.getFilmById(film.getId())
                .orElseThrow(() -> new NotFoundException("Фильм с id " + film.getId() + " не найден"));
        return filmStorage.updateFilm(film);
    }

    public Film getFilmById(int id) {
        return filmStorage.getFilmById(id)
                .orElseThrow(() -> new NotFoundException("Фильм с id " + id + " не найден"));
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public void addLike(int filmId, int userId) {
        Film film = filmStorage.getFilmById(filmId)
                .orElseThrow(() -> new NotFoundException("Фильм не найден"));
        if (!userService.existsById(userId)) {
            throw new UserNotFoundException("Пользователь с id " + userId + " не найден");
        }

        filmStorage.addLikeToFilm(filmId, userId);
    }

    public void removeLike(int filmId, int userId) {
        Film film = filmStorage.getFilmById(filmId)
                .orElseThrow(() -> new NotFoundException("Фильм не найден"));
        if (!userService.existsById(userId)) {
            throw new UserNotFoundException("Пользователь с id " + userId + " не найден");
        }
        film.getLikes().remove(userId);
        filmStorage.updateFilm(film);
    }

    public List<Film> getPopularFilms(int count) {
        return filmStorage.getPopularFilms(count);
    }

    private void validateFilmWithDependencies(Film film) {
        validateFilm(film);

        if (film.getMpa() == null || film.getMpa().getId() == 0) {
            throw new ValidationException("MPA рейтинг не может быть пустым");
        }
        if (!mpaRatingStorage.getMpaById(film.getMpa().getId()).isPresent()) {
            throw new ValidationException("MPA рейтинг с id " + film.getMpa().getId() + " не найден");
        }

        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            for (Genre genre : film.getGenres()) {
                if (!genreStorage.getGenreById(genre.getId()).isPresent()) {
                    throw new ValidationException("Жанр с id " + genre.getId() + " не найден");
                }
            }
        }
    }

    public boolean existsById(int filmId) {
        return filmStorage.getFilmById(filmId).isPresent();
    }
}
