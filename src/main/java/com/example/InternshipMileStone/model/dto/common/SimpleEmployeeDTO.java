package com.example.InternshipMileStone.model.dto.common;


public record SimpleEmployeeDTO(
        Long id,
        String firstName,
        String lastName,
        String email,
        String designation,
        String departmentName
) {
}
