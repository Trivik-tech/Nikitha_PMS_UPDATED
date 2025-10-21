package com.triviktech.repositories.employeereviewrepository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.triviktech.entities.EmployeeReview;
import com.triviktech.entities.kpi.KPI;
import com.triviktech.entities.kra.KRA;
import com.triviktech.entities.krakpi.KraKpi;

import jakarta.transaction.Transactional;

@Repository
public interface EmployeeReviewRepository extends JpaRepository<EmployeeReview, Long> {
    Optional<EmployeeReview> findByKraKpiAndKraAndKpi(KraKpi kraKpi, KRA kra, KPI kpi);

    Optional<EmployeeReview> findByKraKpi_KraKpiIdAndKpi_KpiId(Long kraKpiId, Long kpiId);

    @Modifying
    @Transactional
    @Query("DELETE FROM EmployeeReview e WHERE e.kraKpi = :kraKpi")
    void deleteByKraKpi(@Param("kraKpi") KraKpi kraKpi);

}
