package com.example.InternshipMileStone.model.mappers;

import com.example.InternshipMileStone.model.Department;
import com.example.InternshipMileStone.model.Employee;
import com.example.InternshipMileStone.model.dto.common.SimpleEmployeeDTO;
import com.example.InternshipMileStone.model.dto.request.DepartmentCreateRequestDTO;
import com.example.InternshipMileStone.model.dto.request.DepartmentUpdateDTO;
import com.example.InternshipMileStone.model.dto.response.DepartmentResponseDTO;
import org.mapstruct.*;
import org.springframework.stereotype.Component;

import java.util.List;


//TODO add been validation to DTOs

//TODO  create update EntityFrom DTO for more classes

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.WARN, unmappedTargetPolicy = ReportingPolicy.WARN)
public abstract class DepartmentMapper {


    @Mapping(target = "departmentHead", source = "departmentHead", qualifiedByName = "EmployeeToSummary")
    @Mapping(target = "employeeCount", expression = "java(department.getEmployeeList() != null ? department.getEmployeeList().size() : 0)")
    public abstract DepartmentResponseDTO toResponseDTO(Department department);

    public abstract List<DepartmentResponseDTO> toResponseDTOList(List<Department> departments);

    public abstract Department toEntity(DepartmentCreateRequestDTO dto);


    @Mapping(target = "id", ignore = true)
    public abstract void updateEntityFromDTO(DepartmentUpdateDTO dto, @MappingTarget Department department);

    @Named("EmployeeToSummary")
    public SimpleEmployeeDTO employeeToSummary(Employee employee) {
        if (employee == null) {
            return null;
        }
        return new SimpleEmployeeDTO(
                employee.getId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getEmail(),
                employee.getDesignation(),
                employee.getDepartment() !=null ? employee.getDepartment().getName(): null
        );
    }


}