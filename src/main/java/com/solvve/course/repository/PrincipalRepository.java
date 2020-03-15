package com.solvve.course.repository;

import com.solvve.course.domain.Principal;
import com.solvve.course.domain.constant.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PrincipalRepository extends CrudRepository<Principal, UUID> {

    List<Principal> getAllByRole(Role role);
}
