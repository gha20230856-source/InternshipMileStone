package com.example.InternshipMileStone.repo;


import com.example.InternshipMileStone.model.Employee;
import com.example.InternshipMileStone.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepo  extends JpaRepository<Employee,Long> {

    Optional<List<Employee>> findEmployeesByDepartment_Name(String name);

    Optional<List<Employee>> findAllByStatus(String status);

    Optional<List<Employee>> findEmployeeByDesignation(String designation);

    Optional<Employee> findEmployeeByUser_Username(String userUsername);


    Optional<Employee> findByEmail(String email);
    List<Employee> findEmployeeByStatus(String s);


    @Query("Select e from Employee e where e.salary >= ?1 and e.salary <=?2")
    Collection<Employee> salaryRangeQuery(BigDecimal lower, BigDecimal upper);

    @Query("Select e from Employee e where e.salary >(Select avg(e2.salary) from Employee e2 where e2.department.id = e.department.id)")
    Collection<Employee> employeesAboveTheirDepartmentAverage();

    Optional<Employee> findByUserId(Long userId);

    Optional<Employee> findByUser(User user);
}
