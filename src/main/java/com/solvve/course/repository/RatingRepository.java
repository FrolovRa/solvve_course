package com.solvve.course.repository;

import com.solvve.course.domain.Rating;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RatingRepository extends CrudRepository<Rating, UUID> {
}
