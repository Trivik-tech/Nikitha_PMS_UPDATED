package com.triviktech.repositories.manager;

import com.triviktech.entities.manager.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

public interface ManagerRepository extends JpaRepository<Manager, String> {
    Optional<Manager> findByManagerId(String managerId);
    boolean existsByEmailId(String emailId);
    Optional<Manager> findByEmailId(String emailId);

    @Query("SELECT m FROM Manager m WHERE m.managerId LIKE %:search% OR m.name LIKE %:search% OR m.department.name LIKE %:search% OR m.role LIKE %:search% OR m.category LIKE %:search%")
    List<Manager> searchManagers(@Param("search") String search);

    Optional<Manager> findByName(String rmName);
}