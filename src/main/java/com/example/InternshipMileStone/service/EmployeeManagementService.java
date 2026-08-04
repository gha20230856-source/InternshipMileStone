package com.example.InternshipMileStone.service;


import com.example.InternshipMileStone.model.Department;
import com.example.InternshipMileStone.model.Employee;
import com.example.InternshipMileStone.model.Role;
import com.example.InternshipMileStone.model.User;
import com.example.InternshipMileStone.repo.DepartmentRepo;
import com.example.InternshipMileStone.repo.EmployeeRepo;
import com.example.InternshipMileStone.repo.RoleRepo;
import com.example.InternshipMileStone.repo.UserRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

//TODO remove @Data from services not needed
@Service
@AllArgsConstructor
public class EmployeeManagementService {
    private EmployeeRepo employeeRepo;
    private UserRepo userRepo;
    private DepartmentRepo departmentRepo;
    private RoleRepo roleRepo;


    @Transactional
    public ResponseEntity<String> updateEmployee(Employee employee) {

       if(!employeeRepo.existsById(employee.getId()))
           throw new EntityNotFoundException("no employee with this id ");

       employeeRepo.save(employee);

       return  new ResponseEntity<String>("Employee updated", HttpStatus.ACCEPTED);
    }


    public ResponseEntity<String> addEmployee(Employee employee) {
        if(employeeRepo.findByEmail(employee.getEmail()).isEmpty())
            throw new AccessDeniedException("employee already exists");
        employeeRepo.save(employee);
        return new ResponseEntity<String>("employee added",HttpStatus.OK);
    }

    public ResponseEntity<String> removeEmployee(Long id) {
        employeeRepo.deleteById(id);
        return new ResponseEntity<String>("employee deleted",HttpStatus.OK);
    }

    public ResponseEntity<Collection<Employee>> getEmployees() {
        return new ResponseEntity<>(employeeRepo.findAll(),HttpStatus.OK);
    }
}
