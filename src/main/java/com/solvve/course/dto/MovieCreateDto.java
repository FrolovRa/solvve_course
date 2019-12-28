package com.solvve.course.dto;

import java.util.Objects;

public class MovieCreateDto {
    private String name;
    private String genre;
    private String mainActor;

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
        MovieCreateDto that = (MovieCreateDto) o;
        return name.equals(that.name) &&
                genre.equals(that.genre) &&
                mainActor.equals(that.mainActor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, genre, mainActor);
    }

    @Override
    public String toString() {
        return "MovieCreateDto{" +
                "name='" + name + '\'' +
                ", genre='" + genre + '\'' +
                ", mainActor='" + mainActor + '\'' +
                '}';
    }
}
