package com.example.InternshipMileStone.model.dto.response;

import com.example.InternshipMileStone.model.dto.common.SimpleEmployeeDTO;

public record DepartmentResponseDTO(
        Long id,
        String name,
        String description,
        SimpleEmployeeDTO departmentHead,
        int employeeCount
) {
}