package com.triviktech.repositories.krakpi;

import com.triviktech.entities.employee.EmployeeInformation;
import com.triviktech.entities.krakpi.KraKpi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link KraKpi} entities.
 * Extends {@link JpaRepository} to provide standard CRUD operations
 * and includes custom query methods for KRA/KPI management.
 */
public interface KraKpiRepository extends JpaRepository<KraKpi, Long> {

        Optional<KraKpi> findByEmployeeInformation(EmployeeInformation employee);

        Optional<KraKpi> findFirstByEmployeeInformation(EmployeeInformation employee);

        @Query("SELECT k FROM KraKpi k WHERE k.employeeInformation = :employee AND k.kraKpiId = :kraKpiId")
        Optional<KraKpi> findByEmployeeInformationAndKraKpiId(@Param("employee") EmployeeInformation employee,
                        @Param("kraKpiId") long kraKpiId);

        @Query("SELECT k.selfCompleted FROM KraKpi k WHERE k.employeeInformation.empId = :employeeId")
        Optional<Boolean> findSelfCompletedStatusByEmployeeId(@Param("employeeId") String employeeId);

        /**
         * Check if a {@link KraKpi} exists for a given {@link EmployeeInformation}.
         *
         * @param employee the {@link EmployeeInformation} entity
         * @return {@code true} if exists, {@code false} otherwise
         */
        boolean existsByEmployeeInformation(EmployeeInformation employee);

        @Query("SELECT COUNT(k) > 0 FROM KraKpi k " +
                        "WHERE k.employeeInformation = :employee " +
                        "AND k.createdAt BETWEEN :startDate AND :endDate")
        boolean existsByEmployeeAndCreatedAtBetween(
                        @Param("employee") EmployeeInformation employee,
                        @Param("startDate") java.time.LocalDateTime startDate,
                        @Param("endDate") java.time.LocalDateTime endDate);

        @Query("SELECT k from KraKpi k where k.employeeInformation.empId=:employeeId")
        List<KraKpi> findAllByEmployeeId(@Param("employeeId") String employeeId);

        @Query("SELECT k FROM KraKpi k " +
                        "WHERE FUNCTION('YEAR', k.createdAt) = :year " +
                        "AND k.employeeInformation.empId = :empId")
        List<KraKpi> findByEmployeeAndYear(@Param("empId") String empId,
                        @Param("year") int year);

        @Query("SELECT k FROM KraKpi k " +
                        "WHERE FUNCTION('YEAR', k.createdAt) = :year " +
                        "AND FUNCTION('MONTH', k.createdAt) BETWEEN :startMonth AND :endMonth " +
                        "AND k.employeeInformation.empId = :empId")
        List<KraKpi> findByEmployeeAndQuarter(@Param("empId") String empId,
                        @Param("year") int year,
                        @Param("startMonth") int startMonth,
                        @Param("endMonth") int endMonth);

        @Query("SELECT k FROM KraKpi k " +
                        "WHERE FUNCTION('YEAR', k.createdAt) = :year " +
                        "AND FUNCTION('MONTH', k.createdAt) BETWEEN :startMonth AND :endMonth " +
                        "AND (:month IS NULL OR FUNCTION('MONTH', k.createdAt) = :month) " +
                        "AND k.employeeInformation.empId = :empId")
        List<KraKpi> findByEmployeeAndQuarterAndMonth(@Param("empId") String empId,
                        @Param("year") int year,
                        @Param("startMonth") int startMonth,
                        @Param("endMonth") int endMonth,
                        @Param("month") Integer month);

        @Query("SELECT k.managerCompleted FROM KraKpi k WHERE k.employeeInformation.empId = :employeeId")
        Optional<Boolean> findManagerCompletedStatusByEmployeeId(@Param("employeeId") String employeeId);

        @Query("SELECT k.pmsInitiated FROM KraKpi k WHERE k.employeeInformation.empId = :employeeId")
        Optional<Boolean> findPmsInitiatedByEmployeeId(@Param("employeeId") String employeeId);

        @Query("SELECT k FROM KraKpi k WHERE k.employeeInformation = :employee")
        List<KraKpi> findAllByEmployee(@Param("employee") EmployeeInformation employee);

        List<KraKpi> findAllByEmployeeInformation(EmployeeInformation employeeInformation);

        List<KraKpi> findTopByEmployeeInformationOrderByCreatedAtDesc(EmployeeInformation employee);
        


}
