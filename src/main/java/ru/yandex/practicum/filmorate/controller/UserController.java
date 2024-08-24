package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/users")
public class UserController {
    private final List<User> users = new ArrayList<>();
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final AtomicInteger idCounter = new AtomicInteger(1);

    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody User user) {
        Map<String, String> response = new HashMap<>();
        try {
            if (user.getLogin().contains(" ")) {
                throw new ValidationException("Логин не может содержать пробелы");
            }
            if (!user.getEmail().contains("@")) {
                throw new ValidationException("Некорректный email");
            }
            if (user.getBirthday().isAfter(LocalDate.now())) {
                throw new ValidationException("Дата рождения не может быть в будущем");
            }

            user.setId(idCounter.getAndIncrement());
            if (user.getName() == null || user.getName().isEmpty()) {
                user.setName(user.getLogin());
            }
            users.add(user);
            log.info("Пользователь создан: {}", user);
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        } catch (ValidationException ex) {
            log.warn("Ошибка валидации пользователя: {}", ex.getMessage());
            response.put("error", ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping
    public ResponseEntity<?> updateUser(@Valid @RequestBody User user) {
        Map<String, String> response = new HashMap<>();
        try {
            if (user.getLogin().contains(" ")) {
                throw new ValidationException("Логин не может содержать пробелы");
            }
            if (!user.getEmail().contains("@")) {
                throw new ValidationException("Некорректный email");
            }
            if (user.getBirthday().isAfter(LocalDate.now())) {
                throw new ValidationException("Дата рождения не может быть в будущем");
            }

            for (int i = 0; i < users.size(); i++) {
                if (users.get(i).getId() == user.getId()) {
                    users.set(i, user);
                    log.info("Пользователь обновлен: {}", user);
                    return new ResponseEntity<>(user, HttpStatus.OK);
                }
            }
            log.warn("Пользователь с id {} не найден", user.getId());
            response.put("error", "Пользователь с таким id не найден");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (ValidationException ex) {
            log.warn("Ошибка валидации пользователя: {}", ex.getMessage());
            response.put("error", ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public List<User> getAllUsers() {
        return users;
    }
}