package com.solvve.course.repository;

import com.solvve.course.domain.Principal;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PrincipalRepository extends CrudRepository<Principal, UUID> {

    Principal findByEmail(String email);
}
