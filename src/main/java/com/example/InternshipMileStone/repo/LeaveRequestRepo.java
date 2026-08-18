package com.example.InternshipMileStone.repo;

import com.example.InternshipMileStone.model.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface  LeaveRequestRepo extends JpaRepository<LeaveRequest,Long> {

    @Query("SELECT CASE WHEN COUNT(l) > 0 THEN true ELSE false END " +
            "FROM LeaveRequest l " +
            "WHERE l.applicant.id = :employeeId " +
            "AND :date BETWEEN l.startDate AND l.endDate " +
            "AND l.status = 'APPROVED'")
    boolean isEmployeeOnApprovedLeave(
            @Param("employeeId") Long employeeId,
            @Param("date") LocalDate date
    );
}
