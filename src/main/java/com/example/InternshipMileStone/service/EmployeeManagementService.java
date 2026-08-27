package com.example.InternshipMileStone.service;

import com.example.InternshipMileStone.model.Department;
import com.example.InternshipMileStone.model.Employee;
import com.example.InternshipMileStone.model.Role;
import com.example.InternshipMileStone.model.User;
import com.example.InternshipMileStone.model.dto.common.EmployeeCreatedDTO;
import com.example.InternshipMileStone.model.dto.request.DepartmentUpdateDTO;
import com.example.InternshipMileStone.model.dto.request.EmployeeUpdateDTO;
import com.example.InternshipMileStone.model.dto.response.DepartmentResponseDTO;
import com.example.InternshipMileStone.model.mappers.DepartmentMapper;
import com.example.InternshipMileStone.model.mappers.EmployeeMapper;
import com.example.InternshipMileStone.repo.DepartmentRepo;
import com.example.InternshipMileStone.repo.EmployeeRepo;
import com.example.InternshipMileStone.repo.RoleRepo;
import com.example.InternshipMileStone.repo.UserRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

//TODO seperate response entity from service move it to controller
@Service
@AllArgsConstructor
public class EmployeeManagementService {
    private EmployeeRepo employeeRepo;
    private UserRepo userRepo;
    private DepartmentRepo departmentRepo;
    private RoleRepo roleRepo;
    private UserService userService;
    private EmployeeMapper employeeMapper;
    private DepartmentMapper departmentMapper;

    //employee database operations (basic)

    @Transactional
    public boolean updateEmployee(String username, EmployeeUpdateDTO employee , Boolean admin) {

        User currentUser = userRepo.findByUsername(username).orElseThrow(EntityNotFoundException::new);

        Employee targetEmployee = employeeRepo.findByUser(currentUser).orElseThrow(EntityNotFoundException::new);

        if(employee.firstName() !=null)
            targetEmployee.setFirstName(employee.firstName());

        if(employee.lastName() !=null)
            targetEmployee.setLastName(employee.lastName());

        if(employee.phone() !=null)
            targetEmployee.setPhone(employee.phone());

        if(employee.email() !=null) {

            targetEmployee.setEmail(employee.email());
        }

        if(employee.address() !=null)
            targetEmployee.setAddress(employee.address());

        if(employee.departmentName() !=null&&admin)
            targetEmployee.setDepartment(departmentRepo.findByName(employee.departmentName()).orElseThrow(EntityNotFoundException::new));

        if(employee.designation() !=null &&admin)
            targetEmployee.setDesignation(employee.designation());

        if(employee.status() !=null &&admin )
            targetEmployee.setStatus(employee.status());

        if(employee.salary() !=null && admin)
            targetEmployee.setSalary(employee.salary());

        return true;
    }

    @Transactional
    public String addEmployee(EmployeeCreatedDTO dto) {

        User user = new User();
        user.setUsername(dto.email());
        user.setEmail(dto.email());
        user.setPassword("123"); // Default password
        user.setEnabled(true);

            Role role = roleRepo.findByName("EMPLOYEE")
                    .orElseThrow(() -> new RuntimeException("Role EMPLOYEE NOT found with "));

        user.setRole(role);



        User savedUser = userService.registerUser(user);

        // 2. Create and populate Employee entity
        Employee employee = new Employee();

        employee.setFirstName(dto.firstName());
        employee.setLastName(dto.lastName());
        employee.setEmail(dto.email());
        employee.setDesignation(dto.designation());
        employee.setSalary(dto.salary());
        employee.setStatus("Active");
        employee.setDateOfJoining(LocalDate.now());

        // get department if given
        if (dto.departmentName() != null) {
            Department department = departmentRepo.findByName(dto.departmentName())
                    .orElseThrow(() -> new RuntimeException("Department not found with Name: " + dto.departmentName()));
            employee.setDepartment(department);
        }


        employee.setUser(savedUser);
        employeeRepo.save(employee);

        return "Employee inserted";
    }


    public String removeEmployee(Long id) {
        employeeRepo.deleteById(id);
        return "employee deleted";
    }

    public List<Employee> getEmployees() {
        return employeeRepo.findAll();
    }

    public List<Employee> getEmployees(String department) {
        Department departmentEntity = departmentRepo.findByName(department)
                .orElseThrow(() -> new EntityNotFoundException("no department with this name "));

        return departmentEntity.getEmployeeList();
    }

    public List<Employee> getEmployeesWithDesignation(String d) {
        List<Employee> list = employeeRepo.findEmployeeByDesignation(d).orElse(new ArrayList<Employee>());

        return list;
    }

    public List<Employee> getEmployeesWithStatus(String d) {
        List<Employee> list = employeeRepo.findEmployeeByStatus(d);
        return list ;
    }


    //department opertaions

    public String addDepartment(Department department) {
        if (department.getName() == null)
            throw new EntityNotFoundException("no name for added department");


        if (departmentRepo.findByName(department.getName()).isEmpty())
            departmentRepo.save(department);
        else
            throw new AccessDeniedException("department already exists");

        return "department added";
    }


    @Transactional
    public void removeDepartment(String name) {
        if (departmentRepo.findByName(name).isEmpty())
            throw new EntityNotFoundException("no department with this name exists");
        departmentRepo.deleteByName(name);

    }

    @Transactional
    public void updateDepartment(DepartmentUpdateDTO department) {

        Department departmentEntity = departmentRepo.findByName(department.oldName()).
                orElseThrow(() -> new EntityNotFoundException("no department with this name exists"));
        departmentMapper.updateEntityFromDTO(department, departmentEntity);
    }

    public List<DepartmentResponseDTO> getDepartments() {
        List < Department> departments =  departmentRepo.findAll();

        List<DepartmentResponseDTO> departmentsDTO =
                departmentMapper.toResponseDTOList(departments);

        return departmentsDTO;
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

    public Collection<Employee> salaryRangeQuery(BigDecimal lower, BigDecimal upper) {
        return employeeRepo.salaryRangeQuery(lower, upper);
    }

    public Collection<Employee> employeesAboveTheirDepartmentAverage() {
        return employeeRepo.employeesAboveTheirDepartmentAverage();
    }
    public EmployeeCreatedDTO mapToDTO(Employee employee){

        return new EmployeeCreatedDTO(employee.getFirstName(),employee.getLastName(),employee.getEmail(),
                employee.getDesignation(),employee.getSalary(),employee.getDepartment().getName());
    }
}
