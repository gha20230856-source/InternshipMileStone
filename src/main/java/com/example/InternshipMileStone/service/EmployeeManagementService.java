package com.example.InternshipMileStone.service;


import com.example.InternshipMileStone.model.Department;
import com.example.InternshipMileStone.model.Employee;
import com.example.InternshipMileStone.model.Role;
import com.example.InternshipMileStone.model.User;
import com.example.InternshipMileStone.repo.DepartmentRepo;
import com.example.InternshipMileStone.repo.EmployeeRepo;
import com.example.InternshipMileStone.repo.RoleRepo;
import com.example.InternshipMileStone.repo.UserRepo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Data
@AllArgsConstructor
public class EmployeeManagementService {
    private EmployeeRepo employeeRepo;
    private UserRepo userRepo;
    private DepartmentRepo departmentRepo;
    private RoleRepo roleRepo;



    //Employee required functionality

    public boolean addEmployee( @NonNull Employee employee)
    {
        if(employeeRepo.existsById(employee.getId()))
            return false;

        employeeRepo.save(employee);
        return true;
    }

    public boolean updateEmployee(@NonNull Employee employee){

        if(!employeeRepo.existsById(employee.getId()))
            return false;
        employeeRepo.save(employee);
        return true;

    }

    public boolean deleteEmployee(@NonNull Employee employee){
        if(employeeRepo.existsById(employee.getId()))
            return false;
        else
            employeeRepo.delete(employee);
        return true;
    }

    public List<Employee> AllEmployees(){
        return employeeRepo.findAll();
    }

    public List<Employee> GetEmployeesInDepartment(String name)
    {
        var employees = employeeRepo.findEmployeesByDepartment_Name(name);
        return employees.orElse(new ArrayList<Employee>());
    }

    public List<Employee> GetEmployeesWithStatus(String status)
    {
        var employees = employeeRepo.findAllByStatus(status);
        return employees.orElse(new ArrayList<Employee>());
    }

    public List<Employee> GetEmployeesWithDesignation(String designation)
    {
        var employees = employeeRepo.findEmployeeByDesignation(designation);
        return employees.orElse(new ArrayList<Employee>());
    }

    //Department Requeried functionality

    public List<Department> AllDepartments()
    {
        return departmentRepo.findAll();
    }

    public boolean addDepartment( @NonNull Department department)
    {
        if(departmentRepo.existsById(department.getId()))
            return false;

        departmentRepo.save(department);
        return true;
    }

    public boolean updateDepartment(@NonNull Department department){

        if(!departmentRepo.existsById(department.getId()))
            return false;
        departmentRepo.save(department);
        return true;
    }

    public boolean deleteDepartment(@NonNull Department department){
        if(departmentRepo.existsById(department.getId()))
            return false;
        else
            departmentRepo.delete(department);
        return true;
    }

    // if the employee and the department exist , we set employee with username as head of department
    public boolean assignDepartmentHead(@NonNull String username ,@NonNull String departmentName )
    {
        var new_head = employeeRepo.findEmployeeByUser_Username(username);
        if(new_head.isEmpty())
            return false;

        var department = departmentRepo.findByName(departmentName);
        if(department.isEmpty())
            return false;

        department.get()
                .setDepartmentHead(
                        new_head.get()
                );

        departmentRepo.save(department.get());
        return true;
    }

    public boolean assignEmployeeToDepartment(@NonNull String username ,@NonNull String departmentName){

        var employee = employeeRepo.findEmployeeByUser_Username(username);

        if(employee.isEmpty())
            return false;

        var department = departmentRepo.findByName(departmentName);

        if(department.isEmpty())
            return false;

        employee.get().setDepartment(
                department.get()
        );

        employeeRepo.save(employee.get());
        return true;
    }


    //TODO Remove functions under (they are for testing only)

    public boolean addRole( Role role)
    {
        try {
            roleRepo.save(role);
        } catch (Exception e) {

            System.out.println((e.getMessage()));
            return false;
        }
        return true;

    }


    public boolean addUser( User user)
    {
        try {
            userRepo.save(user);
        } catch (Exception e) {

            System.out.println((e.getMessage()));
            return false;
        }
        return true;

    }




}
