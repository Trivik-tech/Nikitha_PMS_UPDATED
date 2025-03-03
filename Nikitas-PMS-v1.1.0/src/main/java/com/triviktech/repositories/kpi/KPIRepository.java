package com.triviktech.repositories.kpi;

import com.triviktech.entities.kpi.KPI;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KPIRepository extends JpaRepository<KPI, Long> {
}