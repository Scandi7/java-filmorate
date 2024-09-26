package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

@Component
@Qualifier("GenreDbStorage")
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;
    private final GenreMapper genreMapper;

    public GenreDbStorage(JdbcTemplate jdbcTemplate, GenreMapper genreMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreMapper = genreMapper;
    }

    @Override
    public List<Genre> getAllGenres() {
        String sql = "SELECT * FROM genres";
        return jdbcTemplate.query(sql, genreMapper);
    }

    @Override
    public Optional<Genre> getGenreById(int id) {
        String sql = "SELECT * FROM genres WHERE genre_id = ?";
        List<Genre> genres = jdbcTemplate.query(sql, genreMapper, id);
        if (genres.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(genres.get(0));
    }
}
