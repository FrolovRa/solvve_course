package com.solvve.course.repository;

import com.solvve.course.domain.Movie;
import com.solvve.course.dto.movie.MovieFilter;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.StringJoiner;

public class MovieRepositoryCustomImpl implements MovieRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Movie> findByFilter(MovieFilter filter) {
        StringJoiner sj = new StringJoiner(" and ");
        sj.add("select m from Movie m where 1=1");
        if (filter.getGenres() != null && !filter.getGenres().isEmpty()) {
            StringJoiner innerSj = new StringJoiner(" or ");
            filter.getGenres().forEach(genre -> innerSj.add(":" + genre.name() + " in elements(m.genres)"));
            sj.add(innerSj.toString());
        }
        if (filter.getName() != null) {
            sj.add("m.name = :name");
        }
        if (filter.getReleaseDateFrom() != null) {
            sj.add("m.release >= :releaseDateFrom");
        }
        if (filter.getReleaseDateTo() != null) {
            sj.add("m.release < :releaseDateTo");
        }
        TypedQuery<Movie> query = entityManager.createQuery(sj.toString(), Movie.class);
        if (filter.getGenres() != null && !filter.getGenres().isEmpty()) {
            filter.getGenres().forEach(genre -> query.setParameter(genre.name(), genre));
        }
        if (filter.getName() != null) {
            query.setParameter("name", filter.getName());
        }
        if (filter.getReleaseDateFrom() != null) {
            query.setParameter("releaseDateFrom", filter.getReleaseDateFrom());
        }
        if (filter.getReleaseDateTo() != null) {
            query.setParameter("releaseDateTo", filter.getReleaseDateTo());
        }
        return query.getResultList();
    }
}
