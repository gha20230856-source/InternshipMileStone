package com.example.InternshipMileStone.service;


import com.example.InternshipMileStone.model.Employee;
import com.example.InternshipMileStone.model.LeaveRequest;

import com.example.InternshipMileStone.model.dto.common.LeaveBalanceDTO;
import com.example.InternshipMileStone.model.dto.request.LeaveApplyRequestDTO;
import com.example.InternshipMileStone.model.dto.response.LeaveResponseDTO;
import com.example.InternshipMileStone.repo.EmployeeRepo;
import com.example.InternshipMileStone.repo.LeaveRequestRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class  LeaveService {

    private final LeaveRequestRepo leaveRequestRepo;
    private final EmployeeRepo employeeRepo;

    @Value("${leave.annual-allowance:30}")
    private int annualLeaveAllowance;


    @Transactional
    public LeaveResponseDTO applyForLeave(Long userId, LeaveApplyRequestDTO request) {
        Employee applicant = getEmployeeByUserId(userId);

        if (request.startDate().isAfter(request.endDate())) {
            throw new IllegalArgumentException("Start date cannot be after end date.");
        }

        boolean isOverlapping = leaveRequestRepo.hasOverlappingLeave(
                applicant.getId(), request.startDate(), request.endDate());
        if (isOverlapping) {
            throw new IllegalStateException("You already have a pending or approved leave request during this date range.");
        }

        long requestedDays = ChronoUnit.DAYS.between(request.startDate(), request.endDate()) + 1;

        if (!request.leaveType().trim().equalsIgnoreCase("UNPAID") && !request.leaveType().trim().equalsIgnoreCase("SICK") && !request.leaveType().trim().equalsIgnoreCase("EARNED")) {
            LeaveBalanceDTO balance = getLeaveBalance(userId, request.startDate().getYear());
            if (requestedDays > balance.remainingDays()) {
                throw new IllegalStateException("Insufficient leave balance. Remaining: "
                        + balance.remainingDays() + " days, Requested: " + requestedDays + " days.");
            }
        }

        LeaveRequest leaveRequest = new LeaveRequest();
        leaveRequest.setApplicant(applicant);
        leaveRequest.setLeaveType(request.leaveType().trim().toUpperCase());
        leaveRequest.setStartDate(request.startDate());
        leaveRequest.setEndDate(request.endDate());
        leaveRequest.setReason(request.reason());
        leaveRequest.setStatus("PENDING");

        LeaveRequest saved = leaveRequestRepo.save(leaveRequest);
        return mapToDTO(saved);
    }

    //TODO fix
    @Transactional
    public LeaveResponseDTO reviewLeaveRequest(Long reviewerUserId, Long leaveId, String status) {
        String updatedStatus = status.trim().toUpperCase();
        System.out.println("hi");
        if (!updatedStatus.equals("APPROVED") && !updatedStatus.equals("REJECTED")) {
            throw new IllegalArgumentException("Status must be either APPROVED or REJECTED.");
        }

        Employee reviewer = getEmployeeByUserId(reviewerUserId);

        LeaveRequest leaveRequest = leaveRequestRepo.findById(leaveId)
                .orElseThrow(() -> new IllegalArgumentException("Leave request not found with ID: " + leaveId));

        if (!leaveRequest.getStatus().equalsIgnoreCase("PENDING")) {
            throw new IllegalStateException("Leave request has already been processed.");
        }

        leaveRequest.setStatus(updatedStatus);
        leaveRequest.setReviewer(reviewer);

        LeaveRequest saved = leaveRequestRepo.save(leaveRequest);
        return mapToDTO(saved);
    }


    @Transactional(readOnly = true)
    public List<LeaveResponseDTO> getLeavesByEmployee(Long userId) {
        Employee employee = getEmployeeByUserId(userId);
        return leaveRequestRepo.findByApplicantId(employee.getId())
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public List<LeaveResponseDTO> getAllLeaves(String status) {

        if(!(status == null || status.isBlank()
                || status.equalsIgnoreCase("PENDING")
                ||status.equalsIgnoreCase("APPROVED")
                ||status.equalsIgnoreCase("REJECTED"))
        )
            throw new IllegalArgumentException("Status must be either APPROVED or REJECTED or PENDING.");


        List<LeaveRequest> requests = (status != null && !status.isBlank())
                ? leaveRequestRepo.findByStatus(status.trim().toUpperCase())
                : leaveRequestRepo.findAll();

        return requests.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public LeaveBalanceDTO getLeaveBalance(Long userId, Integer year) {
        Employee employee = getEmployeeByUserId(userId);
        int targetYear = (year != null) ? year : LocalDate.now().getYear();

        List<LeaveRequest> approvedLeaves = leaveRequestRepo
                .findApprovedLeavesByEmployeeAndYear(employee.getId(), targetYear);

        long usedDays = approvedLeaves.stream()
                .filter(l -> !l.getLeaveType().trim().equalsIgnoreCase("UNPAID") && !l.getLeaveType().trim().equalsIgnoreCase("SICK")  )
                .mapToLong(l -> ChronoUnit.DAYS.between(l.getStartDate(), l.getEndDate()) + 1)
                .sum();

        long remainingDays = Math.max(0, annualLeaveAllowance - usedDays);

        return new LeaveBalanceDTO(
                employee.getId(),
                employee.getFirstName() + " " + employee.getLastName(),
                targetYear,
                annualLeaveAllowance,
                usedDays,
                remainingDays
        );
    }

    private Employee getEmployeeByUserId(Long userId) {
        return employeeRepo.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("No employee found associated with user ID: " + userId));
    }

    private LeaveResponseDTO mapToDTO(LeaveRequest leaveRequest) {
        long totalDays = ChronoUnit.DAYS.between(leaveRequest.getStartDate(), leaveRequest.getEndDate()) + 1;

        return new LeaveResponseDTO(
                leaveRequest.getId(),
                leaveRequest.getApplicant().getId(),
                leaveRequest.getApplicant().getFirstName() + " " + leaveRequest.getApplicant().getLastName(),
                leaveRequest.getLeaveType(),
                leaveRequest.getStartDate(),
                leaveRequest.getEndDate(),
                totalDays,
                leaveRequest.getReason(),
                leaveRequest.getStatus(),
                leaveRequest.getAppliedOn(),
                leaveRequest.getReviewer() != null ? leaveRequest.getReviewer().getId() : null,
                leaveRequest.getReviewer() != null
                        ? leaveRequest.getReviewer().getFirstName() + " " + leaveRequest.getReviewer().getLastName()
                        : null
        );
    }
}