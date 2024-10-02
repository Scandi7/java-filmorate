package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.mapper.UserRowMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;

import java.util.HashSet;
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
        String sqlUser = "SELECT * FROM users WHERE user_id = ?";
        List<User> users = jdbcTemplate.query(sqlUser, userRowMapper, id);

        if (users.isEmpty()) {
            return Optional.empty();
        }

        User user = users.get(0);

        String sqlFriends = "SELECT friend_id FROM friendships WHERE user_id = ?";
        List<Integer> friendIds = jdbcTemplate.queryForList(sqlFriends, Integer.class, id);

        user.setFriends(new HashSet<>(friendIds));
        return Optional.of(user);
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

    @Override
    public void addFriendship(int userId, int friendId) {
        String sql = "INSERT INTO friendships (user_id, friend_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, userId, friendId);
    }

    @Override
    public void removeFriendship(int userId, int friendId) {
        String sql = "DELETE FROM friendships WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sql, userId, friendId);
    }

    @Override
    public boolean areFriends(int userId, int friendId) {
        String sql = "SELECT COUNT(*) FROM friendships WHERE user_id = ? AND friend_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userId, friendId);
        return count != null && count > 0;
    }
}
