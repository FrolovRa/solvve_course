package com.solvve.course.repository;

import com.solvve.course.domain.Correction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CorrectionRepository extends CrudRepository<Correction, UUID> {

    @Query("SELECT c FROM Correction c")
    List<Correction> getAll();

    List<Correction> getAllByPublicationId(UUID publicationId);
}
