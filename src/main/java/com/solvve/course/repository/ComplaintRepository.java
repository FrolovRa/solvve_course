package com.solvve.course.repository;

import com.solvve.course.domain.MovieReviewComplaint;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ComplaintRepository extends CrudRepository<MovieReviewComplaint, UUID> {
}
