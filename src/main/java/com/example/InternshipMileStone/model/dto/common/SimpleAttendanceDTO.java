package com.example.InternshipMileStone.model.dto.common;

import java.time.LocalDate;
import java.time.LocalDateTime;


public record SimpleAttendanceDTO(
          Long id,
          LocalDate attendanceDate,
          String status,
          LocalDateTime checkInTime,
          LocalDateTime checkOutTime
) {}