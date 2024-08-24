package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import javax.validation.constraints.*;

import java.time.LocalDate;

@Data
public class Film {
    private int id;

    @NotNull(message = "Название фильма не может быть пустым")
    private String name;

    @Size(max = 200, message = "Описание не может превышать 200 символов")
    private String description;

    @NotNull(message = "Дата релиза не может быть пустой")
    private LocalDate releaseDate;

    @Positive(message = "Продолжительность фильма должна быть позитивынм числом")
    private int duration;

    public void setReleaseDate(LocalDate releaseDate) {
        LocalDate earlyReleaseDate = LocalDate.of(1895, 12, 28);
        if (releaseDate.isBefore(earlyReleaseDate)) {
            throw new ValidationException("Дата релиза не может быть раньше 28.12.1895");
        }
        this.releaseDate = releaseDate;
    }
}
