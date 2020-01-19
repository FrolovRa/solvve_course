package com.solvve.course.repository;

import com.solvve.course.domain.Writer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface WriterRepository extends CrudRepository<Writer, UUID> {
}
