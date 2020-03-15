package com.solvve.course.repository;

import com.solvve.course.domain.Correction;
import com.solvve.course.domain.Publication;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CorrectionRepository extends CrudRepository<Correction, UUID> {

    @Query("SELECT c FROM Correction c")
    List<Correction> getAll();

    @Query("SELECT c FROM Correction c WHERE c.id <> ?1 AND c.status = 'NEW'"
            + " AND c.publication = ?2 AND c.selectedText = ?3 AND c.startIndex = ?4")
    List<Correction> getSimilarCorrections(UUID correctionId, Publication publication,
                                           String selectedText, Integer startIndex);

    List<Correction> getAllByPublicationId(UUID publicationId);
}
