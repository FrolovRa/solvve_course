package com.solvve.course.dto.publication;

import lombok.Data;

import java.util.UUID;

@Data
public class PublicationPatchDto {

    private UUID managerId;

    private String content;
}
