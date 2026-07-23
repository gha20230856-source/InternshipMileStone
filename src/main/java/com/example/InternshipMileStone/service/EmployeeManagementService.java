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

import java.time.LocalDate;

@Service
@Data
@AllArgsConstructor
public class EmployeeManagementService {
    private EmployeeRepo employeeRepo;
    private UserRepo userRepo;
    private DepartmentRepo departmentRepo;
    private RoleRepo roleRepo;


    public boolean addEmployee( Employee employee)
    {
        try {
            employeeRepo.save(employee);
        } catch (Exception e) {

            System.out.println((e.getMessage()));
            return false;
        }
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
    public boolean addDepartment( Department department)
    {
        try {
            departmentRepo.save(department);
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
