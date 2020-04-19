package com.solvve.course.repository;

import com.solvve.course.domain.PrincipalRole;
import com.solvve.course.domain.constant.Role;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PrincipalRoleRepository extends CrudRepository<PrincipalRole, UUID> {

    @Query("SELECT role FROM PrincipalRole role")
    List<PrincipalRole> getAll();

    PrincipalRole getByRole(Role role);
}
