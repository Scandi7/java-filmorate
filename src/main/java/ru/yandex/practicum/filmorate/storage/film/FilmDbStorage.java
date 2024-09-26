package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Qualifier;
import ru.yandex.practicum.filmorate.storage.mapper.FilmRowMapper;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

@Component
@Qualifier("filmDbStorage")
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film addFilm(Film film) {
        String sql = "INSERT INTO films (name, description, release_date, duration, mpa_id) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getMpa().getId());
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        String sql = "UPDATE films SET name = ?, description = ?, release_date = ?, duration = ?, mpa_id = ? WHERE film_id = ?";
        jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getMpa().getId(), film.getId());
        return film;
    }

    @Override
    public Optional<Film> getFilmById(int id) {
        String sql = "SELECT * FROM films WHERE film_id = ?";
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, new FilmRowMapper(), id));
    }

    @Override
    public List<Film> getAllFilms() {
        String sql = "SELECT * FROM films";
        return jdbcTemplate.query(sql, new FilmRowMapper());
    }
}