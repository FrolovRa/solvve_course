package com.solvve.course.controller;

import com.solvve.course.dto.complaint.ComplaintCreateDto;
import com.solvve.course.dto.complaint.ComplaintReadDto;
import com.solvve.course.service.ComplaintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/complaints")
public class ComplaintController {

    @Autowired
    private ComplaintService complaintService;

    @GetMapping("/{id}")
    public ComplaintReadDto getComplaint(@PathVariable UUID id) {
        return complaintService.getComplaint(id);
    }

    @PostMapping
    public ComplaintReadDto addComplaint(@RequestBody ComplaintCreateDto complaintCreateDto) {
        return complaintService.addComplaint(complaintCreateDto);
    }

    @DeleteMapping("/{id}")
    public void deleteComplaint(@PathVariable UUID id) {
        complaintService.deleteComplaint(id);
    }
}