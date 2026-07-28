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
//TODO check if @Data is needed
@Service
@Data
@AllArgsConstructor
public class EmployeeManagementService {
    private EmployeeRepo employeeRepo;
    private UserRepo userRepo;
    private DepartmentRepo departmentRepo;
    private RoleRepo roleRepo;



    //Employee required functionality
    //TODO refactor all to throw exceptions instead

    public boolean addEmployee( @NonNull Employee employee)
    {

        employeeRepo.save(employee);
        return true;
    }

    public boolean updateEmployee(@NonNull Employee employee) throws  RuntimeException {

        if(!employeeRepo.existsById(employee.getId()))
            throw new RuntimeException("BAD update");
        employeeRepo.save(employee);
        return true;

    }

    public boolean removeEmployee(@NonNull Employee employee){
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

    public Employee GetEmployeeByUserName(@NonNull String username){
        var employee = employeeRepo.findEmployeeByUser_Username(username);
        return employee.orElseGet(Employee::new);
    }
    //Department Requeried functionality

    public Employee GetEmployeeById(Long id) throws RuntimeException
    {
        Optional<Employee> emp = employeeRepo.findById(id);
        if(emp.isEmpty())
            throw new RuntimeException("no employee exists with this id ");

        return emp.get();
    }

    public List<Department> AllDepartments()
    {
        return departmentRepo.findAll();
    }

    //TODO add role validation for what is under this


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


    public Employee GetEmployeeByEmail(String email) {
        Optional <Employee> emp = employeeRepo.findByEmail(email);
        return emp.orElse(new Employee());
    }

    //TODO remove testing
    public boolean addRole(Role role)
    {
        roleRepo.save(role);
        return true;
    }
}
