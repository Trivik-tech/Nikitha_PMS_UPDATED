package com.triviktech.repositories.employee;

import com.triviktech.entities.employee.EmployeeInformation;
import com.triviktech.entities.manager.Manager;
import org.hibernate.query.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmployeeInformationRepository extends JpaRepository<EmployeeInformation, String> {

    List<EmployeeInformation> findAllByManager(Manager manager);

    Optional<EmployeeInformation> findByEmployeeIdAndManager(String employeeId, Manager manager);

    Optional<EmployeeInformation> findByManager(Manager manager);
}