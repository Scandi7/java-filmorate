package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final List<User> users = new ArrayList<>();
    private final static Logger log = LoggerFactory.getLogger(UserController.class);

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        users.add(user);
        log.info("Пользователь создан: {}", user);
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId() == user.getId()) {
                users.set(i, user);
                log.info("Пользователь обновлен: {}", user);
                return user;
            }
        }
        log.warn("Пользователь с id {} не найден", user.getId());
        throw new ValidationException("Пользователь с таким id не найден");
    }

    @GetMapping
    public List<User> getAllUsers() {
        return users;
    }
}
