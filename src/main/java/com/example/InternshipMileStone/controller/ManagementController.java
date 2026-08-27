package com.example.InternshipMileStone.controller;

import com.example.InternshipMileStone.model.Department;
import com.example.InternshipMileStone.model.Employee;
import com.example.InternshipMileStone.model.dto.common.EmployeeCreatedDTO;
import com.example.InternshipMileStone.model.dto.common.SimpleEmployeeDTO;
import com.example.InternshipMileStone.model.dto.request.DepartmentCreateRequestDTO;
import com.example.InternshipMileStone.model.dto.request.DepartmentUpdateDTO;
import com.example.InternshipMileStone.model.dto.request.EmployeeUpdateDTO;
import com.example.InternshipMileStone.model.dto.response.DepartmentResponseDTO;
import com.example.InternshipMileStone.model.mappers.DepartmentMapper;
import com.example.InternshipMileStone.model.mappers.EmployeeMapper;
import com.example.InternshipMileStone.service.AttendanceService;
import com.example.InternshipMileStone.service.EmployeeManagementService;
import com.example.InternshipMileStone.service.PayrollService;
import com.example.InternshipMileStone.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

//TODO refactor DTO to be inside service ( controllers should be stupid)

//

@RestController
@AllArgsConstructor
public class ManagementController {
    private AttendanceService attendanceService;
    private EmployeeManagementService employeeManagementService;
    private PayrollService payrollService;
    private UserService userService;
    private EmployeeMapper  employeeMapper;
    private DepartmentMapper departmentMapper;




    @PostMapping("/admin/employee")
    ResponseEntity<String> addEmployee(Authentication authentication,@RequestBody EmployeeCreatedDTO request) {
         employeeManagementService.addEmployee(request);

        return new ResponseEntity<>("success", HttpStatus.CREATED);
    }

    @DeleteMapping("/admin/employee/{id}")
    ResponseEntity<String> removeEmployee(@PathVariable Long id) {

         employeeManagementService.removeEmployee(id);

         return ResponseEntity.ok("success");
    }

    @GetMapping("/employees")
    ResponseEntity<Collection<SimpleEmployeeDTO>> getEmployees() {

        List<Employee> employees = employeeManagementService.getEmployees();
        List<SimpleEmployeeDTO> output = employeeMapper.toSimpleDTO(employees);

        return new ResponseEntity<>(output, HttpStatus.OK);
    }
    //uses user email
    @PutMapping("/employee")
    @PreAuthorize(" #target == null or #target == authentication.name or hasRole('ADMIN') ")
    ResponseEntity<String> updateEmployee(Authentication authentication,
            @RequestParam(required = false) String target
            ,@RequestBody EmployeeUpdateDTO employee) {

        if (target == null) {target = authentication.getName();}


        boolean result =  employeeManagementService.updateEmployee(target,employee,hasRole(authentication,"ADMIN"));

        return new ResponseEntity<>("success", HttpStatus.OK);


    }


    //	Search/filter employees by department, designation, or status (active/inactive)

    @GetMapping("/employees/{department}")
    ResponseEntity<Collection<SimpleEmployeeDTO>> getEmployees(@PathVariable String department) {
        List<Employee> list =  employeeManagementService.getEmployees(department);

        List<SimpleEmployeeDTO> DTOs = employeeMapper.toSimpleDTO(list);

        return new ResponseEntity<>(DTOs , HttpStatus.OK);
    }

    @GetMapping("/employees/designation")
    ResponseEntity<Collection<SimpleEmployeeDTO>> getEmployeesWithDesignation(@RequestParam(required = false) String designation) {

        if(designation == null)
            return getEmployees();

        List<Employee> list =  employeeManagementService.getEmployeesWithDesignation(designation);
        List<SimpleEmployeeDTO> DTOs = employeeMapper.toSimpleDTO(list);

        return new ResponseEntity<>(DTOs , HttpStatus.OK);
    }

    @GetMapping("/employees/status")
    ResponseEntity<Collection<SimpleEmployeeDTO>> getEmployeesWithStatus(@RequestParam(required = false) String status) {

        if(status == null)
            return getEmployees();

        List<Employee> list =  employeeManagementService.getEmployeesWithStatus(status);
        List<SimpleEmployeeDTO> DTOs = employeeMapper.toSimpleDTO(list);

        return new ResponseEntity<>(DTOs , HttpStatus.OK);
    }

    @PostMapping("/admin/department")
    ResponseEntity<String> addDepartment(@RequestBody DepartmentCreateRequestDTO departmentDTO) {

        Department department  = departmentMapper.toEntity(departmentDTO);
         employeeManagementService.addDepartment(department);

         return new ResponseEntity<>("success", HttpStatus.CREATED);
    }

    @DeleteMapping("/admin/department")
    ResponseEntity<String> deleteDepartment(@RequestParam String name) {
        employeeManagementService.removeDepartment(name);
        return ResponseEntity.ok("department " + name + "deleted");
    }

    @PutMapping("/admin/department")
    ResponseEntity<String> updateDepartment(@RequestBody DepartmentUpdateDTO department) {
        employeeManagementService.updateDepartment(department);
        return ResponseEntity.ok("department updated");
    }

    @GetMapping("/departments")
    public ResponseEntity<Collection<DepartmentResponseDTO>> getDepartments() {
        List<DepartmentResponseDTO > departments  = (employeeManagementService.getDepartments());

        return ResponseEntity.ok(departments);
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

    @GetMapping("/admin/employeeSalary")
    public ResponseEntity<Collection<Employee>> getEmployeesInSalaryRange(@RequestParam(required = false,defaultValue = "0") BigDecimal lower,
                                                                          @RequestParam(required = false, defaultValue = "10000000") BigDecimal upper) {
        return ResponseEntity.ok(employeeManagementService.salaryRangeQuery(lower, upper));
    }

    @GetMapping("/admin/employeeSalaryAboveDepartmentAverage")
    ResponseEntity<Collection<Employee>> getEmployeesAboveDepartmentAverage() {
        return ResponseEntity.ok(employeeManagementService.employeesAboveTheirDepartmentAverage());
    }

    //helpers

    private boolean hasRole(Authentication authentication, String role) {
        return authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_" + role));
    }


}
