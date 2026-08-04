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
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

//TODO seperate response entity from service move it to controller
@Service
@AllArgsConstructor
public class EmployeeManagementService {
    private EmployeeRepo employeeRepo;
    private UserRepo userRepo;
    private DepartmentRepo departmentRepo;
    private RoleRepo roleRepo;

    //employee database operations (basic)

    @Transactional
    public ResponseEntity<String> updateEmployee(Employee employee) {

       if(!employeeRepo.existsById(employee.getId()))
           throw new EntityNotFoundException("no employee with this id ");

       employeeRepo.save(employee);

       return  new ResponseEntity<String>("Employee updated", HttpStatus.ACCEPTED);
    }


    public ResponseEntity<String> addEmployee(Employee employee) {
        if(employeeRepo.findByEmail(employee.getEmail()).isPresent())
            throw new AccessDeniedException("employee already exists");
        User existingUser = userRepo.findByUsername(employee.getUser().getUsername());
        employee.setUser(existingUser);
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

    public ResponseEntity<Collection<Employee>> getEmployees(String department) {
        Department departmentEntity = departmentRepo.findByName(department)
                .orElseThrow(()-> new EntityNotFoundException("no department with this name ") );

        return new ResponseEntity<>(departmentEntity.getEmployeeList(),HttpStatus.OK);
    }

    public ResponseEntity<Collection<Employee>> getEmployeesWithDesignation(String d) {
        Collection<Employee> list = employeeRepo.findEmployeeByDesignation(d).orElse(new ArrayList<Employee>());
        return new ResponseEntity<>(list,HttpStatus.OK);
    }
    public ResponseEntity<Collection<Employee>> getEmployeesWithStatus(String d) {
        Collection<Employee> list = employeeRepo.findEmployeeByStatus(d);
        return new ResponseEntity<>(list,HttpStatus.OK);
    }


    //department opertaions

    public ResponseEntity<String> addDepartment(Department department) {
        if(department.getName() == null)
            throw  new EntityNotFoundException("no name for added department");
        department.setId(null);
        if(departmentRepo.findByName(department.getName()).isEmpty())
            departmentRepo.save(department);
        else
            throw new AccessDeniedException("department already exists");

        return new ResponseEntity<>("department added",HttpStatus.CREATED);
    }


    @Transactional
    public void removeDepartment(String name)  {
        if(departmentRepo.findByName(name).isEmpty())
            throw new EntityNotFoundException("no department with this name exists");
        departmentRepo.deleteByName(name);

    }
    @Transactional
    public void updateDepartment(Department department) {

        Department departmentEntity = departmentRepo.findByName(department.getName()).
                orElseThrow(() -> new EntityNotFoundException("no department with this name exists"));
        departmentEntity.setDescription(department.getDescription());
        departmentEntity.setDepartmentHead(department.getDepartmentHead());

    }

    public Collection<Department> getDepartments() {
        return departmentRepo.findAll();
    }


    @Transactional
    public void updateDepartmentHead(String departmentName, String email) {
        Department departmentEntity = departmentRepo.findByName(departmentName).
                orElseThrow(() -> new EntityNotFoundException("no department with this name exists"));
        Employee employeeEntity = employeeRepo.findByEmail(email).
                orElseThrow(() -> new EntityNotFoundException("no employee with this name exists"));
        departmentEntity.setDepartmentHead(employeeEntity);
    }


    @Transactional
    public void updateEmployeeDepartment(String email, String departmentName) {
        Department departmentEntity = departmentRepo.findByName(departmentName).
                orElseThrow(() -> new EntityNotFoundException("no department with this name exists"));
        Employee employeeEntity = employeeRepo.findByEmail(email).
                orElseThrow(() -> new EntityNotFoundException("no employee with this name exists"));
        employeeEntity.setDepartment(departmentEntity);

    }
}
