package com.example.InternshipMileStone.model.dto.request;

import jakarta.validation.constraints.NotBlank;

public record DepartmentUpdateDTO(
        @NotBlank
        String oldName,
        @NotBlank
        String name,
        
        String description
) {
}
