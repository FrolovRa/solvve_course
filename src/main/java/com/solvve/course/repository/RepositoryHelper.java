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
        E entity;
        try {
            entity = entityManager.getReference(entityClass, id);
        } catch (javax.persistence.EntityNotFoundException e) {
            throw new EntityNotFoundException(entityClass, id);
        }
        return entity;
    }

    public <E> E getEntityRequired(Class<E> entityClass, UUID id) {
        Optional<E> optional = Optional.ofNullable(entityManager.find(entityClass, id));
        return optional.orElseThrow(() -> new EntityNotFoundException(entityClass, id));
    }
}