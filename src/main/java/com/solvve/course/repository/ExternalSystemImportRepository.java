package com.solvve.course.repository;

import com.solvve.course.domain.ExternalSystemImport;
import com.solvve.course.domain.constant.ImportedEntityType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ExternalSystemImportRepository extends CrudRepository<ExternalSystemImport, UUID> {

    ExternalSystemImport findByIdInExternalSystemAndEntityType(String idInExternalSystem,
                                                                  ImportedEntityType entityType);
}