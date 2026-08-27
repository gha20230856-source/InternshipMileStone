package com.example.InternshipMileStone.model.dto.request;

public record DepartmentUpdateDTO(
        String oldName,
        String name,
        String description
) {
}
