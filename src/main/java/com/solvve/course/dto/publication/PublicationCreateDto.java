package com.solvve.course.dto.publication;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class PublicationCreateDto {

    @NotNull
    private UUID managerId;

    @NotNull
    private String content;
}
