package com.solvve.course.dto.complaint;

import com.solvve.course.domain.constant.ComplaintReason;
import lombok.Data;

import java.util.UUID;

@Data
public class ComplaintCreateDto {

    private UUID userId;

    private UUID entityId;

    private ComplaintReason reason;
}
