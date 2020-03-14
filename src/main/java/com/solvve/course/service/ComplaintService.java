package com.solvve.course.service;

import com.solvve.course.domain.Complaint;
import com.solvve.course.dto.complaint.ComplaintCreateDto;
import com.solvve.course.dto.complaint.ComplaintReadDto;
import com.solvve.course.repository.ComplaintRepository;
import com.solvve.course.repository.RepositoryHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ComplaintService {

    @Autowired
    private TranslationService translationService;

    @Autowired
    private ComplaintRepository complaintRepository;

    @Autowired
    private RepositoryHelper repositoryHelper;

    public ComplaintReadDto getComplaint(UUID id) {
        Complaint complaintFromDb = repositoryHelper.getEntityRequired(Complaint.class, id);

        return translationService.toReadDto(complaintFromDb);
    }

    public ComplaintReadDto addComplaint(ComplaintCreateDto userCreateDto) {
        Complaint complaint = translationService.toEntity(userCreateDto);
        complaint = complaintRepository.save(complaint);

        return translationService.toReadDto(complaint);
    }

    public void deleteComplaint(UUID id) {
        complaintRepository.delete(repositoryHelper.getEntityRequired(Complaint.class, id));
    }
}
