package com.example.InternshipMileStone.model.dto.request;


import jakarta.validation.constraints.NotBlank;

public record DepartmentCreateRequestDTO(
        @NotBlank
        String name,
        String description
) {
}
