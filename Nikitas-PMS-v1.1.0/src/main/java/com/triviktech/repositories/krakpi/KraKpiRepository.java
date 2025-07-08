package com.triviktech.repositories.krakpi;

import com.triviktech.entities.employee.EmployeeInformation;
import com.triviktech.entities.krakpi.KraKpi;
import com.triviktech.entities.manager.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface KraKpiRepository extends JpaRepository<KraKpi, Long> {

    Optional<KraKpi> findByEmployeeInformation(EmployeeInformation employee);

    @Query("SELECT k FROM KraKpi k WHERE k.employeeInformation = :employee AND k.kraKpiId = :kraKpiId")
    Optional<KraKpi> findByEmployeeInformationAndKraKpiId(@Param("employee") EmployeeInformation employee,
                                                          @Param("kraKpiId") long kraKpiId);

    @Query("SELECT k.selfCompleted FROM KraKpi k WHERE k.employeeInformation.empId = :employeeId")
    Optional<Boolean> findSelfCompletedStatusByEmployeeId(@Param("employeeId") String employeeId);

    boolean existsByEmployeeInformation(EmployeeInformation employeeId);
}
