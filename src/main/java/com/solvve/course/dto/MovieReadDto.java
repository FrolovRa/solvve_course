package com.solvve.course.dto;

import java.util.Objects;
import java.util.UUID;

public class MovieReadDto {

    private UUID id;
    private double rating;
    private String name;
    private String genre;
    private String mainActor;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
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
        MovieReadDto that = (MovieReadDto) o;
        return Double.compare(that.rating, rating) == 0 &&
                id.equals(that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(genre, that.genre) &&
                Objects.equals(mainActor, that.mainActor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, rating, name, genre, mainActor);
    }

    @Override
    public String toString() {
        return "MovieReadDto{" +
                "id=" + id +
                ", rating=" + rating +
                ", name='" + name + '\'' +
                ", genre='" + genre + '\'' +
                ", mainActor='" + mainActor + '\'' +
                '}';
    }
}
