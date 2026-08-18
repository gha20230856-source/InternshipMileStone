package com.example.InternshipMileStone.model.dto.request;



import java.time.LocalDate;

public record LeaveApplyRequestDTO(
        String leaveType,
        LocalDate startDate,
        LocalDate endDate,
        String reason
) {}
