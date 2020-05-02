package com.solvve.course.service.importer;

import com.solvve.course.domain.AbstractEntity;
import com.solvve.course.domain.Actor;
import com.solvve.course.domain.ExternalSystemImport;
import com.solvve.course.domain.Movie;
import com.solvve.course.domain.constant.ImportedEntityType;
import com.solvve.course.exception.ImportAlreadyPerformedException;
import com.solvve.course.repository.ExternalSystemImportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ExternalSystemImportService {

    @Autowired
    private ExternalSystemImportRepository externalSystemImportRepository;

    public void validateNotImported(Class<? extends AbstractEntity> entityToImport,
                                    String idInExternalSystem) throws ImportAlreadyPerformedException {
        ImportedEntityType importedEntityType = getImportedEntityType(entityToImport);
        ExternalSystemImport esi = externalSystemImportRepository.findByIdInExternalSystemAndEntityType(
            idInExternalSystem,
            importedEntityType
        );

        if (esi != null) {
            throw new ImportAlreadyPerformedException(esi);
        }
    }

    public <T extends AbstractEntity> UUID createExternalSystemImport(T entity, String idInExternalSystem) {
        ImportedEntityType entityType = getImportedEntityType(entity.getClass());
        ExternalSystemImport esi = new ExternalSystemImport();
        esi.setEntityType(entityType);
        esi.setEntityId(entity.getId());
        esi.setIdInExternalSystem(idInExternalSystem);

        esi = externalSystemImportRepository.save(esi);

        return esi.getId();
    }

    private ImportedEntityType getImportedEntityType(Class<? extends AbstractEntity> entityClass) {
        if (Movie.class.equals(entityClass)) {
            return ImportedEntityType.MOVIE;
        } else if (Actor.class.equals(entityClass)) {
            return ImportedEntityType.ACTOR;
        }

        throw new IllegalArgumentException("Importing of entities " + entityClass + " is not supported");
    }
}