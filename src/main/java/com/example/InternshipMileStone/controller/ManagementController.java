package com.example.InternshipMileStone.controller;

import com.example.InternshipMileStone.model.Employee;
import com.example.InternshipMileStone.model.Role;
import com.example.InternshipMileStone.model.User;
import com.example.InternshipMileStone.service.AttendanceService;
import com.example.InternshipMileStone.service.EmployeeManagementService;
import com.example.InternshipMileStone.service.PayrollService;
import com.example.InternshipMileStone.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@AllArgsConstructor
public class ManagementController {
    private AttendanceService attendanceService;
    private EmployeeManagementService employeeManagementService;
    private PayrollService payrollService;
    private UserService userService;



    @PostMapping("/admin/user")
    ResponseEntity<String> registerUser(@RequestBody User user)
    {
        return userService.registerUser(user);
    }

    @PostMapping("/admin/employee")
    ResponseEntity<String> addEmployee(@RequestBody Employee employee)
    {
        return employeeManagementService.addEmployee(employee);
    }

    @DeleteMapping("/admin/employee/{id}")
    ResponseEntity<String> removeEmployee(@PathVariable Long id)
    {
        return employeeManagementService.removeEmployee(id);

    }

    @PutMapping("/employee")
    @PreAuthorize("#employee?.getId() == principle.id or hadRole('Admin')")
    ResponseEntity<String> updateEmployee(@RequestBody Employee employee)
    {
        return employeeManagementService.updateEmployee(employee);
    }

    @GetMapping("/employees")
    ResponseEntity<Collection<Employee>> getEmployees()
    {
        return employeeManagementService.getEmployees();
    }

    //	Search/filter employees by department, designation, or status (active/inactive)

    @GetMapping("/employee")


}
