package com.solvve.course.dto.complaint;

import com.solvve.course.domain.constant.ComplaintReason;
import com.solvve.course.dto.user.UserReadDto;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class ComplaintReadDto {

    private UUID id;

    private Instant createdAt;

    private Instant updatedAt;

    private UserReadDto user;

    private UUID entityId;

    private ComplaintReason reason;
}
