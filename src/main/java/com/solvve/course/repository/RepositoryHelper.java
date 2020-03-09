package com.solvve.course.repository;

import com.solvve.course.exception.EntityNotFoundException;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;
import java.util.UUID;

@Component
public class RepositoryHelper {

    @PersistenceContext
    private EntityManager entityManager;

    public <E> E getReferenceIfExist(Class<E> entityClass, UUID id) {
        validateExist(entityClass, id);
        return entityManager.getReference(entityClass, id);
    }

    private <E> void validateExist(Class<E> entityClass, UUID id) {
        Number count = (Number) entityManager
                .createQuery("SELECT count(e) FROM " + entityClass.getSimpleName() + " e WHERE e.id = :id")
                .setParameter("id", id)
                .getSingleResult();
        if (count.intValue() < 1) {
            throw new EntityNotFoundException(entityClass, id);
        }
    }

    public <E> E getEntityRequired(Class<E> entityClass, UUID id) {
        Optional<E> optional = Optional.ofNullable(entityManager.find(entityClass, id));
        return optional.orElseThrow(() -> new EntityNotFoundException(entityClass, id));
    }
}