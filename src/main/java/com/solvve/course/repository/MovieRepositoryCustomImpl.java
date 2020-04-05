package com.solvve.course.repository;

import com.solvve.course.domain.Movie;
import com.solvve.course.dto.movie.MovieFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.StringJoiner;

public class MovieRepositoryCustomImpl implements MovieRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<Movie> findByFilter(MovieFilter filter, Pageable pageable) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT m FROM Movie m WHERE 1=1");
        Query query = createQueryApplyingFilter(filter, pageable.getSort(), sb);
        applyPageable(query, pageable);

        List<Movie> data = query.getResultList();

        final long count = getCountOfMovies(filter);

        return new PageImpl<>(data, pageable, count);
    }

    private long getCountOfMovies(MovieFilter filter) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT count(m) FROM Movie m WHERE 1=1");

        Query query = createQueryApplyingFilter(filter, null, sb);
        return ((Number) query.getSingleResult()).longValue();
    }

    private void applyPageable(Query query, Pageable pageable) {
        if (pageable.isPaged()) {
            query.setMaxResults(pageable.getPageSize());
            query.setFirstResult((int) pageable.getOffset());
        }
    }

    private Query createQueryApplyingFilter(MovieFilter filter, Sort sort, StringBuilder sb) {
        if (filter.getGenres() != null && !filter.getGenres().isEmpty()) {
            StringJoiner innerSj = new StringJoiner(" OR ");
            filter.getGenres().forEach(genre -> innerSj.add(":" + genre.name() + " IN ELEMENTS(m.genres)"));
            sb.append(" AND ").append(innerSj.toString());
        }
        if (filter.getActorId() != null) {
            sb.append(" AND EXISTS (SELECT a.id FROM m.cast a WHERE a.id = :actorId)");
        }
        if (filter.getName() != null) {
            sb.append(" AND m.name = :name");
        }
        if (filter.getReleaseDateFrom() != null) {
            sb.append(" AND m.release >= :releaseDateFrom");
        }
        if (filter.getReleaseDateTo() != null) {
            sb.append(" AND m.release < :releaseDateTo");
        }
        if (sort != null && sort.isSorted()) {
            sb.append(" order by ");
            for (Sort.Order order : sort.toList()) {
                sb.append("v.").append(order.getProperty()).append(" ").append(order.getDirection().toString());
            }
        }
        Query query = entityManager.createQuery(sb.toString());
        if (filter.getGenres() != null && !filter.getGenres().isEmpty()) {
            filter.getGenres().forEach(genre -> query.setParameter(genre.name(), genre));
        }
        if (filter.getActorId() != null) {
            query.setParameter("actorId", filter.getActorId());
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

        return query;
    }
}
