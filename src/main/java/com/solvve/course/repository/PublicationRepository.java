package com.solvve.course.repository;

import com.solvve.course.domain.Publication;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PublicationRepository extends CrudRepository<Publication, UUID> {
}
