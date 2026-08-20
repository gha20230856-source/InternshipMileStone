package com.example.InternshipMileStone.model.mappers;

import com.example.InternshipMileStone.model.Employee;
import com.example.InternshipMileStone.model.dto.common.EmployeeCreatedDTO;
import jakarta.security.auth.message.MessagePolicy;
import lombok.Data;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.context.annotation.Bean;

@Mapper(componentModel = "spring",unmappedSourcePolicy = ReportingPolicy.IGNORE,unmappedTargetPolicy = ReportingPolicy.ERROR)
public abstract class EmployeeMapper {

    @Mapping(target = "departmentName" , expression = "java(employee.getDepartment() != null ? employee.getDepartment().getName() : null )")
    public abstract EmployeeCreatedDTO employeetoEmployeeCreatedDTO(Employee employee);
}


