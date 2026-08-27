package com.example.InternshipMileStone.model.mappers;

import com.example.InternshipMileStone.model.Department;
import com.example.InternshipMileStone.model.Employee;
import com.example.InternshipMileStone.model.dto.common.EmployeeCreatedDTO;
import com.example.InternshipMileStone.model.dto.common.SimpleEmployeeDTO;
import com.example.InternshipMileStone.model.dto.request.EmployeeUpdateDTO;
import com.example.InternshipMileStone.repo.DepartmentRepo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring",unmappedSourcePolicy = ReportingPolicy.WARN,unmappedTargetPolicy = ReportingPolicy.WARN)
public abstract class EmployeeMapper {

    //TODO learn how to fix
    @Autowired
    private  DepartmentRepo departmentRepo;



    @Mapping(target = "departmentName" , expression = "java(employee.getDepartment() != null ? employee.getDepartment().getName() : null )")
    public abstract EmployeeCreatedDTO toDTO(Employee employee);


    @Mapping(target ="department",source = "departmentName" ,qualifiedByName = "DepartmentNameToDepartment")
    public abstract  Employee toEntity(EmployeeCreatedDTO employeeCreatedDTO);

    @Named("DepartmentNameToDepartment")
    public Department departmentNameToDepartment(String departmentName) {

        return departmentRepo.findByName(departmentName).orElse(null);
    }

    public abstract  List<EmployeeCreatedDTO> toDTO(List<Employee> employees);

    @Mapping(target = "departmentName" , expression = "java(employee.getDepartment() != null ? employee.getDepartment().getName() : null )")
    public abstract EmployeeUpdateDTO toUpdateDTO(Employee employee);


    @Mapping(target = "departmentName" , expression = "java(employee.getDepartment() != null ? employee.getDepartment().getName() : null )")
    public abstract SimpleEmployeeDTO  toSimpleDTO(Employee employee);

    public abstract  List<SimpleEmployeeDTO> toSimpleDTO(List<Employee> employees);


}


