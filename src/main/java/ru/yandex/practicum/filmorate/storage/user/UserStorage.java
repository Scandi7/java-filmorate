package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    User addUser(User user);

    User updateUser(User user);

    Optional<User> getUserById(int id);

    List<User> getAllUsers();

    List<User> getFriends(int userId);

    void addFriendship(int userId, int friendId);

    void removeFriendship(int userId, int friendId);

    boolean areFriends(int userId, int friendId);
}