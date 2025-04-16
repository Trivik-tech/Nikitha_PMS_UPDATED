package com.triviktech.repositories.department;

import com.triviktech.entities.department.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    boolean existsByName(String department);
}