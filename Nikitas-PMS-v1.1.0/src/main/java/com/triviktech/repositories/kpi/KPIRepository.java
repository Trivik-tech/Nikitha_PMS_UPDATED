package com.triviktech.repositories.kpi;

import com.triviktech.entities.kpi.KPI;
import com.triviktech.entities.kra.KRA;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface KPIRepository extends JpaRepository<KPI, Long> {
    Optional<KPI> findByDescriptionIgnoreCaseAndKra(String description, KRA kra);
}