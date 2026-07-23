package com.example.InternshipMileStone.repo;

import com.example.InternshipMileStone.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepo extends JpaRepository<Department,Long> {



}
