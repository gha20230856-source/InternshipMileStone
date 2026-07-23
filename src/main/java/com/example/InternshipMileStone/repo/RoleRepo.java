package com.example.InternshipMileStone.repo;

import com.example.InternshipMileStone.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepo extends JpaRepository<Role,Long> {
}
