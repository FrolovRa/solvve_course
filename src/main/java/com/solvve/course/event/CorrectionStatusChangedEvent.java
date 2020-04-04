package com.solvve.course.event;

import com.solvve.course.domain.Correction;
import com.solvve.course.domain.constant.CorrectionStatus;
import lombok.Data;

import java.util.UUID;

@Data
public class CorrectionStatusChangedEvent {
    private UUID correctionId;
    private CorrectionStatus newStatus;

    public CorrectionStatusChangedEvent(Correction correction) {
        this.correctionId = correction.getId();
        this.newStatus = correction.getStatus();
    }
}
