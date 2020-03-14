package com.solvve.course.repository;

import com.solvve.course.domain.Publication;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface PublicationRepository extends CrudRepository<Publication, UUID> {
}
