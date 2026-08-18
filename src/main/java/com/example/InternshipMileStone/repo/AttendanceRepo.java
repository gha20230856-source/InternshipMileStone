package com.example.InternshipMileStone.repo;

import com.example.InternshipMileStone.model.Attendance;
import com.example.InternshipMileStone.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepo extends JpaRepository<Attendance,Long> {

    Optional<Attendance> findByEmployeeIdAndAttendanceDate(Long employeeId, LocalDate attendanceDate);

    boolean existsByEmployeeIdAndAttendanceDate(Long employeeId, LocalDate attendanceDate);

    // Flexible query to search history by employee, department, and/or date range
    @Query( "SELECT a FROM Attendance a WHERE (:employeeId IS NULL OR a.employee.id = :employeeId) "+
            "AND (:departmentId IS NULL OR a.employee.department.id = :departmentId)"+
            "AND ( :endDate IS NULL OR :startDate is NULL OR (a.attendanceDate BETWEEN :startDate AND :endDate) )" )
    List<Attendance> findAttendanceHistory(
            @Param("employeeId") Long employeeId,
            @Param("departmentId") Long departmentId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

        @Query("Select e FROM Employee e where e NOT IN (SELECT a.employee FROM Attendance a" +
                " where a.attendanceDate = :Date) ")
    List<Employee> findNotAttendingEmployees(@Param("Date")LocalDate date);


}
