package ru.yandex.practicum.filmorate.model;

import ru.yandex.practicum.filmorate.exception.ValidationException;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Objects;


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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof Film film)) return false;
        return id == film.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Film{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", releaseDate=" + releaseDate +
                ", duration=" + duration +
                '}';
    }
}
