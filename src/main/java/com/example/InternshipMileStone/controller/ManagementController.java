package com.example.InternshipMileStone.controller;

import com.example.InternshipMileStone.model.Department;
import com.example.InternshipMileStone.model.Employee;
import com.example.InternshipMileStone.model.Role;
import com.example.InternshipMileStone.model.User;
import com.example.InternshipMileStone.model.dto.common.EmployeeCreatedDTO;
import com.example.InternshipMileStone.model.mappers.EmployeeMapper;
import com.example.InternshipMileStone.service.AttendanceService;
import com.example.InternshipMileStone.service.EmployeeManagementService;
import com.example.InternshipMileStone.service.PayrollService;
import com.example.InternshipMileStone.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

//TODO decouple service and controller

//TODO learn how to use DTO's correctly and fix your usage of requestBody (it is a bad practise)

@RestController
@AllArgsConstructor
public class ManagementController {
    private AttendanceService attendanceService;
    private EmployeeManagementService employeeManagementService;
    private PayrollService payrollService;
    private UserService userService;
    private EmployeeMapper  employeeMapper;


    @PostMapping("/hard")
    EmployeeCreatedDTO employeetoEmployeeCreatedDTO(@RequestBody Employee employee) {
        return employeeMapper.employeetoEmployeeCreatedDTO(employee);
    }

    @PostMapping("/admin/employee")
    ResponseEntity<String> addEmployee(@RequestBody EmployeeCreatedDTO request) {
         employeeManagementService.addEmployee(request);
        return new ResponseEntity<>("success", HttpStatus.CREATED);
    }

    @DeleteMapping("/admin/employee/{id}")
    ResponseEntity<String> removeEmployee(@PathVariable Long id) {
        return employeeManagementService.removeEmployee(id);

    }

    @PutMapping("/employee")
    @PreAuthorize("#employee?.getId() == principle.id or hadRole('Admin')")
    ResponseEntity<String> updateEmployee(@RequestBody Employee employee) {
        return employeeManagementService.updateEmployee(employee);
    }

    @GetMapping("/employees")
    ResponseEntity<Collection<Employee>> getEmployees() {

        return employeeManagementService.getEmployees();

    }

    //	Search/filter employees by department, designation, or status (active/inactive)

    @GetMapping("/employees/{department}")
    ResponseEntity<Collection<Employee>> getEmployees(@PathVariable String department) {
        return employeeManagementService.getEmployees(department);
    }

    @GetMapping("/employees/designation/{d}")
    ResponseEntity<Collection<Employee>> getEmployeesWithDesignation(@PathVariable String d) {
        return employeeManagementService.getEmployeesWithDesignation(d);
    }

    @GetMapping("/employees/status/{d}")
    ResponseEntity<Collection<Employee>> getEmployeesWithStatus(@PathVariable String d) {
        return employeeManagementService.getEmployeesWithStatus(d);
    }

    @PostMapping("/admin/department")
    ResponseEntity<String> addDepartment(@RequestBody Department department) {
        return employeeManagementService.addDepartment(department);
    }

    @DeleteMapping("/admin/department/{name}")
    ResponseEntity<String> deleteDepartment(@PathVariable String name) {
        employeeManagementService.removeDepartment(name);
        return ResponseEntity.ok("department " + name + "deleted");
    }

    @PutMapping("/admin/department")
    ResponseEntity<String> updateDepartment(@RequestBody Department department) {
        employeeManagementService.updateDepartment(department);
        return ResponseEntity.ok("department updated");
    }

    @GetMapping("/departments")
    public ResponseEntity<Collection<Department>> getDepartments() {
        return ResponseEntity.ok(employeeManagementService.getDepartments());
    }

    //send me the department name and the new manager email I assign him
    @PutMapping("/admin/department/{name}/head/{email}")
    public ResponseEntity<String> updateHead(@PathVariable String name, @PathVariable String email) {
        employeeManagementService.updateDepartmentHead(name, email);
        return ResponseEntity.ok("head updated");
    }

    @PutMapping("/admin/employee/{email}/{department}")
    public ResponseEntity<String> updateEmployeeDepartment(@PathVariable String email, @PathVariable String department) {
        employeeManagementService.updateEmployeeDepartment(email, department);
        return ResponseEntity.ok("head updated");
    }

    @GetMapping("/admin/employeeSalary/{lower}/{upper}")
    public ResponseEntity<Collection<Employee>> getEmployeesInSalRange(@PathVariable Long lower, @PathVariable Long upper) {
        return ResponseEntity.ok(employeeManagementService.salaryRangeQuery(lower, upper));
    }

    @GetMapping("/admin/employeeSalaryAboveDepartmentAverage")
    ResponseEntity<Collection<Employee>> getEmployeesAboveDepartmentAverage() {
        return ResponseEntity.ok(employeeManagementService.employeesAboveTheirDepartmentAverage());
    }


}
