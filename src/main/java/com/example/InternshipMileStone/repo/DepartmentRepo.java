package com.example.InternshipMileStone.repo;

import com.example.InternshipMileStone.model.Department;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartmentRepo extends JpaRepository<Department,Long> {

    Optional<Department> findByName(@NonNull String departmentName);
}
