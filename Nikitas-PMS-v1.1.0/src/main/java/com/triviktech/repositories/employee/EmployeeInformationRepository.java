package com.triviktech.repositories.employee;

import com.triviktech.entities.employee.EmployeeInformation;
import com.triviktech.entities.manager.Manager;
import org.hibernate.query.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface EmployeeInformationRepository extends JpaRepository<EmployeeInformation, String> {


    @Query("SELECT e from EmployeeInformation e WHERE e.empId LIKE %:search% OR e.name LIKE %:search% OR e.department.name LIKE %:search% OR e.reportingManager LIKE %:search% OR e.role LIKE %:search% OR e.category LIKE %:search%")
    List<EmployeeInformation> searchEmployees(String search);

    List<EmployeeInformation> findByEmpIdIn(Set<String> empIds);

    Optional<EmployeeInformation> findByReportingManagerAndEmpId(String managerName, String employeeId);
}

