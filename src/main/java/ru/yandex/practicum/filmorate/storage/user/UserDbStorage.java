package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.mapper.UserRowMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;

import java.util.List;
import java.util.Optional;

@Component
@Qualifier("UserDbStorage")
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;
    private final UserRowMapper userRowMapper;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate, UserRowMapper userRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.userRowMapper = userRowMapper;
    }

    @Override
    public User addUser(User user) {
        String sql = "INSERT INTO users (email, login, name, birthday) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday());
        return user;
    }

    @Override
    public User updateUser(User user) {
        String sql = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE user_id = ?";
        int updatedRows = jdbcTemplate.update(sql, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());

        if (updatedRows == 0) {
            throw new UserNotFoundException("Пользователь с id " + user.getId() + " не найден");
        }
        return user;
    }

    @Override
    public Optional<User> getUserById(int id) {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        List<User> users = jdbcTemplate.query(sql, userRowMapper, id);
        if (users.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(users.get(0));
    }

    @Override
    public List<User> getAllUsers() {
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, userRowMapper);
    }

    @Override
    public List<User> getFriends(int userId) {
        String sql = "SELECT * FROM users WHERE user_id IN (SELECT friend_id FROM friendships WHERE user_id = ?)";
        return jdbcTemplate.query(sql, userRowMapper, userId);
    }
}
