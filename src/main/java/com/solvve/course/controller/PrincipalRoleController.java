package com.solvve.course.controller;

import com.solvve.course.dto.role.PrincipalRoleReadDto;
import com.solvve.course.service.PrincipalRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class PrincipalRoleController {

    @Autowired
    private PrincipalRoleService principalRoleService;

    @GetMapping("/roles")
    public List<PrincipalRoleReadDto> getAllRoles() {
        return principalRoleService.getAllRoles();
    }

    @PostMapping("/principals/{principalId}/roles/{roleId}")
    public List<PrincipalRoleReadDto> addRoleToPrincipal(@PathVariable UUID principalId,
                                                         @PathVariable UUID roleId) {
        return principalRoleService.addRoleToPrincipal(principalId, roleId);
    }
}