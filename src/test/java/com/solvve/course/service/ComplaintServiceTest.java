package com.solvve.course.service;

import com.solvve.course.domain.Complaint;
import com.solvve.course.domain.constant.ComplaintReason;
import com.solvve.course.dto.complaint.ComplaintCreateDto;
import com.solvve.course.dto.complaint.ComplaintReadDto;
import com.solvve.course.exception.EntityNotFoundException;
import com.solvve.course.repository.ComplaintRepository;
import com.solvve.course.util.TestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
@Sql(statements = {
        "delete from complaint",
        "delete from user"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ComplaintServiceTest {

    @Autowired
    private ComplaintRepository complaintRepository;

    @Autowired
    private TestUtils utils;

    @Autowired
    private TranslationService translationService;

    @Autowired
    private ComplaintService complaintService;

    @Test
    public void testGetComplaint() {
        Complaint complaint = utils.getComplaintFromDb();
        ComplaintReadDto actualComplaint = translationService.toReadDto(complaint);

        ComplaintReadDto complaintReadDto = complaintService.getComplaint(complaint.getId());

        assertThat(actualComplaint).isEqualToComparingFieldByField(complaintReadDto);
    }

    @Test
    public void testAddComplaint() {
        ComplaintCreateDto createDto = utils.createComplaintCreateDto();
        createDto.setUserId(utils.getUserFromDb().getId());

        utils.inTransaction(() -> {
            ComplaintReadDto readDto = complaintService.addComplaint(createDto);

            assertThat(createDto).isEqualToIgnoringGivenFields(readDto,
                    "userId", "updatedAt", "createdAt");
            assertNotNull(readDto.getId());
            assertEquals(readDto.getUser().getId(), createDto.getUserId());

            ComplaintReadDto userFromDb = complaintService.getComplaint(readDto.getId());
            assertThat(readDto).isEqualToIgnoringGivenFields(userFromDb);
            assertEquals(readDto.getUser(), userFromDb.getUser());
        });
    }

    @Test
    public void testDeleteComplaint() {
        Complaint complaint = utils.getComplaintFromDb();

        complaintService.deleteComplaint(complaint.getId());

        assertFalse(complaintRepository.existsById(complaint.getId()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteByWrongId() {
        complaintService.deleteComplaint(UUID.randomUUID());
    }

    @Test
    public void testCreatedAtIsSet() {
        Complaint complaint = new Complaint();
        complaint.setUser(utils.getUserFromDb());
        complaint.setEntityId(UUID.randomUUID());

        complaint = complaintRepository.save(complaint);

        Instant createdAtBeforeReload = complaint.getCreatedAt();
        assertNotNull(createdAtBeforeReload);
        complaint = complaintRepository.findById(complaint.getId()).get();

        Instant createdAtAfterReload = complaint.getCreatedAt();
        assertNotNull(createdAtAfterReload);
        assertEquals(createdAtBeforeReload, createdAtAfterReload);
    }

    @Test
    public void testUpdatedAtIsSet() {
        Complaint complaint = new Complaint();
        complaint.setUser(utils.getUserFromDb());
        complaint.setEntityId(UUID.randomUUID());
        complaint.setReason(ComplaintReason.TYPO);

        complaint = complaintRepository.save(complaint);

        Instant updatedAtBeforeReload = complaint.getCreatedAt();
        assertNotNull(updatedAtBeforeReload);
        complaint = complaintRepository.findById(complaint.getId()).get();

        Instant updatedAtAfterReload = complaint.getCreatedAt();
        assertNotNull(updatedAtAfterReload);
        assertEquals(updatedAtBeforeReload, updatedAtAfterReload);

        complaint.setReason(ComplaintReason.SPAM);
        complaint = complaintRepository.save(complaint);
        Instant updatedAtAfterUpdate = complaint.getUpdatedAt();

        assertNotEquals(updatedAtAfterUpdate, updatedAtAfterReload);
    }
}
