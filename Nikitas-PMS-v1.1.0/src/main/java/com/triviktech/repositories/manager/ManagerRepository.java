package com.triviktech.repositories.manager;

import com.triviktech.entities.manager.Manager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ManagerRepository extends JpaRepository<Manager, String> {
    Optional<Manager> findByManagerId(String managerId);
}