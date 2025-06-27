package com.triviktech.repositories.hr;

import com.triviktech.entities.hr.HR;
import com.triviktech.entities.manager.Manager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HRRepository extends JpaRepository<HR, String> {
    boolean existsByEmail(String officialEmailId);
    Optional<HR> findByEmail(String email);

}