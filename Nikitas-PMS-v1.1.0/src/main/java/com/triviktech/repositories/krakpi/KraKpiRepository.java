package com.triviktech.repositories.krakpi;

import com.triviktech.entities.employee.EmployeeInformation;
import com.triviktech.entities.krakpi.KraKpi;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KraKpiRepository extends JpaRepository<KraKpi, Long> {
    KraKpi findByEmployeeInformation(EmployeeInformation employee);
}