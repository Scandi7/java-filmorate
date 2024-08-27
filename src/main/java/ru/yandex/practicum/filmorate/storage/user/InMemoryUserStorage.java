package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class InMemoryUserStorage implements UserStorage {

    private final List<User> users = new ArrayList<>();

    @Override
    public Optional<User> getUserById(int id) {
        return users.stream().filter(user -> user.getId() == id).findFirst();
    }

    @Override
    public User updateUser(User user) {
        Optional<User> existingUser = getUserById(user.getId());
        if (existingUser.isPresent()) {
            int index = users.indexOf(existingUser.get());
            users.set(index, user);
            return user;
        } else {
            throw new UserNotFoundException("User not found");
        }
    }

    @Override
    public User addUser(User user) {
        users.add(user);
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }
}

