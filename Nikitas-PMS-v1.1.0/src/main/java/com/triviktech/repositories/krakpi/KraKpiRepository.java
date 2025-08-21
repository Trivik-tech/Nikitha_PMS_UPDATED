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

    /**
     * Find the {@link KraKpi} record for a specific {@link EmployeeInformation}.
     *
     * @param employee the {@link EmployeeInformation} entity
     * @return an {@link Optional} containing the {@link KraKpi} if found
     */
    Optional<KraKpi> findByEmployeeInformation(EmployeeInformation employee);

    /**
     * Find the first {@link KraKpi} record for a specific {@link EmployeeInformation}.
     * Useful if an employee has multiple KRA/KPI entries and only the first is required.
     *
     * @param employee the {@link EmployeeInformation} entity
     * @return an {@link Optional} containing the first {@link KraKpi} if found
     */
    Optional<KraKpi> findFirstByEmployeeInformation(EmployeeInformation employee);

    /**
     * Find a {@link KraKpi} by {@link EmployeeInformation} and KRA/KPI ID.
     *
     * @param employee   the {@link EmployeeInformation} entity
     * @param kraKpiId   the unique KRA/KPI ID
     * @return an {@link Optional} containing the matching {@link KraKpi} if found
     */
    @Query("SELECT k FROM KraKpi k WHERE k.employeeInformation = :employee AND k.kraKpiId = :kraKpiId")
    Optional<KraKpi> findByEmployeeInformationAndKraKpiId(@Param("employee") EmployeeInformation employee,
                                                          @Param("kraKpiId") long kraKpiId);

    /**
     * Get the self-completed status of a KRA/KPI for a specific employee.
     *
     * @param employeeId the employee ID
     * @return an {@link Optional} containing {@code true} if completed, {@code false} otherwise
     */
    @Query("SELECT k.selfCompleted FROM KraKpi k WHERE k.employeeInformation.empId = :employeeId")
    Optional<Boolean> findSelfCompletedStatusByEmployeeId(@Param("employeeId") String employeeId);

    /**
     * Check if a {@link KraKpi} exists for a given {@link EmployeeInformation}.
     *
     * @param employee the {@link EmployeeInformation} entity
     * @return {@code true} if exists, {@code false} otherwise
     */
    boolean existsByEmployeeInformation(EmployeeInformation employee);

    /**
     * Check if a {@link KraKpi} exists for an {@link EmployeeInformation}
     * within a given date range.
     *
     * @param employee  the {@link EmployeeInformation} entity
     * @param startDate the start date of the range
     * @param endDate   the end date of the range
     * @return {@code true} if a record exists, {@code false} otherwise
     */
    @Query("SELECT COUNT(k) > 0 FROM KraKpi k " +
            "WHERE k.employeeInformation = :employee " +
            "AND k.createdAt BETWEEN :startDate AND :endDate")
    boolean existsByEmployeeAndCreatedAtBetween(
            @Param("employee") EmployeeInformation employee,
            @Param("startDate") java.time.LocalDateTime startDate,
            @Param("endDate") java.time.LocalDateTime endDate
    );

    /**
     * Find all {@link KraKpi} records by employee ID.
     *
     * @param employeeId the employee ID
     * @return a list of {@link KraKpi} objects
     */
    @Query("SELECT k from KraKpi k where k.employeeInformation.empId=:employeeId")
    List<KraKpi> findAllByEmployeeId(@Param("employeeId") String employeeId);
}
