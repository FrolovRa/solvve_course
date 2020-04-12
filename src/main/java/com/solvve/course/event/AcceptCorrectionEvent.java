package com.solvve.course.event;

import lombok.Data;

import java.util.UUID;

@Data
public class AcceptCorrectionEvent {
    private final UUID correctionId;

    public AcceptCorrectionEvent(UUID correctionId) {
        this.correctionId = correctionId;
    }
}