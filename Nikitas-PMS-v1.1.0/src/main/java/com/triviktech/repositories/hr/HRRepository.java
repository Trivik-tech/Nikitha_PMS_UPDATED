package com.triviktech.repositories.hr;

import com.triviktech.entities.hr.HR;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HRRepository extends JpaRepository<HR, String> {
}