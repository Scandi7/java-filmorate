package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static ru.yandex.practicum.filmorate.validation.ValidateUser.validateUser;

@Service
public class UserService {
    private final UserStorage userStorage;
    private final AtomicInteger idCounter = new AtomicInteger(1);

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User createUser(User user) {
        validateUser(user);
        user.setId(idCounter.getAndIncrement());
        return userStorage.addUser(user);
    }

    public User addUser(User user) {
        if (user.getLogin().contains(" ") || !user.getEmail().contains("@")) {
            throw new ValidationException("Неверные данные пользователя");
        }
        return userStorage.addUser(user);
    }

    public boolean existsById(int userId) {
        return userStorage.getUserById(userId).isPresent();
    }

    public User updateUser(User user) {
        validateUser(user);
        Optional<User> existingUser = userStorage.getUserById(user.getId());
        if (existingUser.isPresent()) {
            userStorage.updateUser(user);
            return user;
        } else {
            throw new UserNotFoundException("Пользователь с таким id не найден");
        }
    }

    public boolean areFriends(int userId, int friendId) {
        User user = userStorage.getUserById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с id " + userId + " не найден"));
        return user.getFriends().contains(friendId);
    }

    public Optional<User> getUserById(int id) {
        return userStorage.getUserById(id);
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public void addFriend(int userId, int friendId) {
        User user = userStorage.getUserById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с id " + userId + " не найден"));
        User friend = userStorage.getUserById(friendId)
                .orElseThrow(() -> new UserNotFoundException("Друг с id " + friendId + " не найден"));

        if (user.getFriends().contains(friendId)) {
            throw new ValidationException("Пользователь уже добавлен в друзья");
        }
        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
        userStorage.updateUser(user);
        userStorage.updateUser(friend);
    }

    public void removeFriend(int userId, int friendId) {
        User user = userStorage.getUserById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с id " + userId + " не найден"));
        User friend = userStorage.getUserById(friendId)
                .orElseThrow(() -> new UserNotFoundException("Друг с id " + friendId + " не найден"));

        if (!user.getFriends().contains(friendId)) {
            return;
        }
        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
        userStorage.updateUser(user);
        userStorage.updateUser(friend);
    }

    public List<User> getFriends(int userId) {
        return userStorage.getFriends(userId);
    }

    public List<User> getCommonFriends(int userId, int otherId) {
        User user = userStorage.getUserById(userId)
                .orElseThrow(() -> new ValidationException("Пользователь не найден"));
        User otherUser = userStorage.getUserById(otherId)
                .orElseThrow(() -> new ValidationException(
                        "Другой пользователь не найден"));

        Set<Integer> commonFriendsIds = new HashSet<>(user.getFriends());
        commonFriendsIds.retainAll(otherUser.getFriends());
        List<User> commonFriends = new ArrayList<>();
        for (int friendId : commonFriendsIds) {
            userStorage.getUserById(friendId).ifPresent(commonFriends::add);
        }
        return commonFriends;
    }
}