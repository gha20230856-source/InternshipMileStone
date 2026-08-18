package com.example.InternshipMileStone.service;


import com.example.InternshipMileStone.model.Attendance;
import com.example.InternshipMileStone.model.Employee;

import com.example.InternshipMileStone.model.dto.response.AttendanceResponseDTO;
import com.example.InternshipMileStone.repo.AttendanceRepo;
import com.example.InternshipMileStone.repo.DepartmentRepo;
import com.example.InternshipMileStone.repo.EmployeeRepo;
import com.example.InternshipMileStone.repo.LeaveRequestRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepo attendanceRepo;
    private final EmployeeRepo employeeRepo;
    private final LeaveRequestRepo leaveRequestRepo;
    private final DepartmentRepo departmentRepo;


    @Value("${attendance.required-hours}")
    private double requiredHours;


    @Transactional
    public AttendanceResponseDTO checkIn(Long employeeId) {
        LocalDate today = LocalDate.now();

        attendanceRepo.findByEmployeeIdAndAttendanceDate(employeeId, today)
                .ifPresent(a -> {
                    throw new IllegalStateException("Employee has already checked in today.");
                });

        Employee employee = employeeRepo.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found with ID: " + employeeId));

        Attendance attendance = new Attendance();
        attendance.setEmployee(employee);
        attendance.setAttendanceDate(today);
        attendance.setCheckInTime(LocalDateTime.now());
        attendance.setStatus("PENDING");

        Attendance saved = attendanceRepo.save(attendance);
        return mapToDTO(saved);
    }


    @Transactional
    public AttendanceResponseDTO checkOut(Long employeeId) {
        LocalDate today = LocalDate.now();

        Attendance attendance = attendanceRepo.findByEmployeeIdAndAttendanceDate(employeeId, today)
                .orElseThrow(() -> new IllegalStateException("No check-in record found for today. Please check in first."));

        if (attendance.getCheckOutTime() != null) {
            throw new IllegalStateException("Employee has already checked out today.");
        }

        LocalDateTime checkOutTime = LocalDateTime.now();
        attendance.setCheckOutTime(checkOutTime);

        // Calculate hours worked
        double hoursWorked = Duration.between(attendance.getCheckInTime(), checkOutTime).toMinutes() / 60.0;

        // Dynamic status check based on configurable hours
        if (hoursWorked >= requiredHours) {
            attendance.setStatus("PRESENT");
        } else {
            attendance.setStatus("HALF_DAY");
        }

        Attendance updated = attendanceRepo.save(attendance);
        return mapToDTO(updated);
    }



    @Scheduled(cron = "0 0 0 * * 1-5")
    @Transactional
    public void processDailyAttendance() {
        processDailyAttendance(LocalDate.now().minusDays(1));
    }


    @Transactional
    public void processDailyAttendance(LocalDate date) {
        List<Employee> absentEmployees = attendanceRepo.findNotAttendingEmployees( date);

        for (Employee employee : absentEmployees) {

                Attendance attendance = new Attendance();
                attendance.setEmployee(employee);
                attendance.setAttendanceDate(date);


                boolean isOnLeave = leaveRequestRepo.isEmployeeOnApprovedLeave(employee.getId(), date);

                if (isOnLeave) {
                    attendance.setStatus("LEAVE");
                } else {
                    attendance.setStatus("ABSENT");
                }

                attendanceRepo.save(attendance);

        }
    }



    //TODO ask what paramter to replace employeeId with??
    @Transactional(readOnly = true)
    public List<AttendanceResponseDTO> getAttendanceHistory(Long employeeId, String departmentName, LocalDate startDate, LocalDate endDate) {
        // Default  if some value doesn't exist ( == NULL) return all that apply to rest of conditions


       Long departmentId = departmentRepo.findByName(departmentName)
               .orElseThrow( ()-> new EntityNotFoundException("no department with this name")).getId();

        return attendanceRepo.findAttendanceHistory(employeeId, departmentId, startDate, endDate)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }


    private AttendanceResponseDTO mapToDTO(Attendance attendance) {
        Double hoursWorked = null;
        if (attendance.getCheckInTime() != null && attendance.getCheckOutTime() != null) {
            hoursWorked = Duration.between(attendance.getCheckInTime(), attendance.getCheckOutTime()).toMinutes() / 60.0;
            hoursWorked = Math.round(hoursWorked * 100.0) / 100.0; // Round to 2 decimals
        }

        return new AttendanceResponseDTO(attendance.getId()
                ,(attendance.getEmployee().getId())
                ,(attendance.getEmployee().getFirstName() + " " + attendance.getEmployee().getLastName())
                ,(attendance.getEmployee().getDepartment() != null ? attendance.getEmployee().getDepartment().getName() : null)
                ,(attendance.getAttendanceDate())
                ,(attendance.getStatus())
                ,(attendance.getCheckInTime())
                ,(attendance.getCheckOutTime())
                ,(hoursWorked))
                ;
    }
}