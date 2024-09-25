package ru.yandex.practicum.filmorate.model;

import java.util.Objects;

public class MpaRating {
    private int id;
    private String rating;
    private String description;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "MpaRating{" +
                "id=" + id +
                ", rating='" + rating + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MpaRating mpaRating)) return false;
        return id == mpaRating.id && Objects.equals(rating, mpaRating.rating) && Objects.equals(description, mpaRating.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, rating, description);
    }
}
