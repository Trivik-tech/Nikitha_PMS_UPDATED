package com.triviktech.repositories.employee;

import com.triviktech.entities.employee.EmployeeInformation;
import com.triviktech.entities.manager.Manager;
import org.hibernate.query.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.*;
import java.util.List;

public interface EmployeeInformationRepository extends JpaRepository<EmployeeInformation, String> {

    @Query("SELECT e from EmployeeInformation e WHERE e.empId LIKE %:search% OR e.name LIKE %:search% OR e.department.name LIKE %:search% OR e.reportingManager LIKE %:search% OR e.role LIKE %:search% OR e.category LIKE %:search%")

    List<EmployeeInformation> searchEmployees(@Param("search") String search);

    List<EmployeeInformation> findByEmpIdIn(Set<String> empIds);

    Optional<EmployeeInformation> findByReportingManagerAndEmpId(String managerName, String employeeId);

    @Query("SELECT e.department.name, COUNT(e) FROM EmployeeInformation e GROUP BY e.department.name")
    List<Object[]> countEmployeesByDepartment();

    @Query("select e from EmployeeInformation e where e.reportingManager=:reportingManager")
    List<EmployeeInformation> findAllByReportingManager(@Param("reportingManager") String reportingManager);

    Optional<EmployeeInformation> findByOfficialEmailId(String officialEmailId);

}
