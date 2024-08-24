package ru.yandex.practicum.filmorate.modelTest;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    private final Validator validator;

    public UserTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void emailIsInvalid() {
        User user = new User();
        user.setEmail("Invalid email");
        user.setLogin("login");
        user.setBirthday(LocalDate.of(2020, 1, 1));

        assertFalse(validator.validate(user).isEmpty());
    }

    @Test
    void birthdayInFuture() {
        User user = new User();
        user.setEmail("email@email.com");
        user.setLogin("login");
        user.setBirthday(LocalDate.now().plusDays(1));

        assertFalse(validator.validate(user).isEmpty());
    }
}
