package ru.yandex.practicum.filmorate.model;

import java.util.Objects;

public class MpaRating {
    private int id;
    private String name;
    private String description;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "MpaRating{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MpaRating mpaRating)) return false;
        return id == mpaRating.id && Objects.equals(name, mpaRating.name) && Objects.equals(description, mpaRating.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description);
    }
}
