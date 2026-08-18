package com.example.InternshipMileStone.repo;

import com.example.InternshipMileStone.model.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

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

    List<LeaveRequest> findByApplicantId(Long applicantId);

    List<LeaveRequest> findByStatus(String status);

    // Fetch approved leaves in a given year
    @Query("SELECT l FROM LeaveRequest l " +
            "WHERE l.applicant.id = :employeeId " +
            "AND l.status = 'APPROVED' " +
            "AND YEAR(l.startDate) = :year")
    List<LeaveRequest> findApprovedLeavesByEmployeeAndYear(
            @Param("employeeId") Long employeeId,
            @Param("year") int year
    );




    @Query("SELECT CASE WHEN COUNT(l) > 0 THEN true ELSE false END " +
            "FROM LeaveRequest l " +
            "WHERE l.applicant.id = :employeeId " +
            "AND l.status != 'REJECTED' " +
            "AND (:startDate BETWEEN l.startDate AND l.endDate " +
            "OR :endDate BETWEEN l.startDate AND l.endDate " +
            "OR l.startDate BETWEEN :startDate AND :endDate)")
    boolean hasOverlappingLeave(
            @Param("employeeId") Long employeeId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}
