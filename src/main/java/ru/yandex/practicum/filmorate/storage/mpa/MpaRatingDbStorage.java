package ru.yandex.practicum.filmorate.storage.mpa;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.mapper.MpaRatingMapper;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.List;
import java.util.Optional;

@Component
@Qualifier("MpaRatingDbStorage")
public class MpaRatingDbStorage implements MpaRatingStorage {

    private final JdbcTemplate jdbcTemplate;
    private final MpaRatingMapper mpaRatingMapper;

    public MpaRatingDbStorage(JdbcTemplate jdbcTemplate, MpaRatingMapper mpaRatingMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.mpaRatingMapper = mpaRatingMapper;
    }

    @Override
    public List<MpaRating> getAllMpa(int limit) {
        String sql = "SELECT * FROM mpa_ratings ORDER BY mpa_id ASC LIMIT ?";
        return jdbcTemplate.query(sql, mpaRatingMapper, limit);
    }

    @Override
    public Optional<MpaRating> getMpaById(int id) {
        String sql = "SELECT * FROM mpa_ratings WHERE mpa_id = ?";
        List<MpaRating> mpaList = jdbcTemplate.query(sql, mpaRatingMapper, id);
        return mpaList.stream().findFirst();
    }
}
