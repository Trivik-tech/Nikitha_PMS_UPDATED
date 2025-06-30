package com.triviktech.repositories.hr;

import com.triviktech.entities.hr.HR;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HRRepository extends JpaRepository<HR, String> {

    Optional<HR> findByName(String hrName);

    boolean existsByEmail(String officialEmailId);

    Optional<HR> findByEmail(String email);
}
