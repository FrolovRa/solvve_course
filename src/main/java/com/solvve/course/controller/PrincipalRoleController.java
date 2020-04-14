package com.solvve.course.controller;

import com.solvve.course.dto.role.PrincipalRoleReadDto;
import com.solvve.course.service.PrincipalRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/principals/{principalId}")
public class PrincipalRoleController {

    @Autowired
    private PrincipalRoleService principalRoleService;

    @RequestMapping("/roles/{roleId}")
    public PrincipalRoleReadDto addRoleToPrincipal(@PathVariable UUID principalId,
                                                   @PathVariable UUID roleId) {
        return principalRoleService.addRoleToPrincipal(principalId, roleId);
    }
}
