package com.solvve.course.repository;

import com.solvve.course.domain.Director;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DirectorRepository extends CrudRepository<Director, UUID> {
}
