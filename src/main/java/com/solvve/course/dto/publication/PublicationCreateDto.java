package com.solvve.course.dto.publication;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

@Data
public class PublicationCreateDto {

    @NotNull
    private UUID managerId;

    @NotNull
    @Size(max = 2500)
    private String content;
}
