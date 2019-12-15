package com.solvve.course.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;
import java.util.UUID;

@Entity
public class Movie {
    @Id
    @GeneratedValue
    private UUID id;

    private float rating;

    private String name;

    private String genre;

    private String mainActor;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getMainActor() {
        return mainActor;
    }

    public void setMainActor(String mainActor) {
        this.mainActor = mainActor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movie movie = (Movie) o;
        return Float.compare(movie.rating, rating) == 0 &&
                id.equals(movie.id) &&
                name.equals(movie.name) &&
                genre.equals(movie.genre) &&
                mainActor.equals(movie.mainActor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, rating, name, genre, mainActor);
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", rating=" + rating +
                ", name='" + name + '\'' +
                ", genre='" + genre + '\'' +
                ", mainActor='" + mainActor + '\'' +
                '}';
    }
}
