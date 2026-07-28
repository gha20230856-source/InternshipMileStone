package com.example.InternshipMileStone.controller;

import com.example.InternshipMileStone.model.Employee;
import com.example.InternshipMileStone.model.Role;
import com.example.InternshipMileStone.model.User;
import com.example.InternshipMileStone.service.AttendanceService;
import com.example.InternshipMileStone.service.EmployeeManagementService;
import com.example.InternshipMileStone.service.PayrollService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@AllArgsConstructor
public class ManagementController {
    private AttendanceService attendanceService;
    private EmployeeManagementService employeeManagementService;
    private PayrollService payrollService;

    @GetMapping("/Employee/{username}")
    public Employee getEmployee(@PathVariable String username)
    {
        return employeeManagementService.GetEmployeeByUserName(username);
    }
    @PostMapping("/Employee")
    public String insertEmployee(@RequestBody Employee employee){
        try
        {
            employeeManagementService.addEmployee(employee);

        }
        catch (Exception ex)
        {
            return "insertion failed";
        }

        return "insertion succeeded";
    }

    @PutMapping("/Employee")
    public String updateEmployee(@RequestBody Employee employee)
    {
        try
        {
            employeeManagementService.updateEmployee(employee);

        }
        catch (Exception ex)
        {
            return "update failed";
        }
        return "success";
    }

    @DeleteMapping("/Employee/{username}")
    public String deleteEmployee(@PathVariable String username)
    {
        try {
            employeeManagementService.removeEmployee(employeeManagementService.GetEmployeeByUserName(username));
        }
        catch(Exception ex)
        {
            return ex.getMessage();
        }

        return "success";
    }

    @GetMapping("/Employees")
    public Collection<Employee> getEmployees()
    {
        return employeeManagementService.AllEmployees();
    }
    @PostMapping("/role")
    public String addRole(@RequestBody Role role)
    {
        employeeManagementService.addRole(role);
        return "success";
    }
    @PostMapping("/user")
    public String adduser(@RequestBody User user)
    {
        employeeManagementService.addUser(user);
        return "success";
    }



    @GetMapping("test")
    public String test(HttpServletRequest session) {
        return "test"+ session.getSession().getId();
    }





}
