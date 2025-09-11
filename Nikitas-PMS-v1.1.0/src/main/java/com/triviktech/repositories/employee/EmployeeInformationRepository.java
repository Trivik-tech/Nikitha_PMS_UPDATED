package com.triviktech.repositories.employee;

import com.triviktech.entities.employee.EmployeeInformation;
import com.triviktech.entities.manager.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface EmployeeInformationRepository extends JpaRepository<EmployeeInformation, String> {

    @Query("SELECT e FROM EmployeeInformation e " +
            "LEFT JOIN e.department d " +
            "LEFT JOIN e.manager m " +
            "WHERE LOWER(e.empId) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(e.name) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(d.name) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(m.name) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(e.role) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(e.category) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<EmployeeInformation> searchEmployees(@Param("search") String search);

    List<EmployeeInformation> findByEmpIdIn(Set<String> empIds);

    Optional<EmployeeInformation> findByManagerAndEmpId(Manager manager, String employeeId);

    @Query("SELECT e.department.name, COUNT(e) FROM EmployeeInformation e GROUP BY e.department.name")
    List<Object[]> countEmployeesByDepartment();

    List<EmployeeInformation> findAllByManager(Manager manager);

    boolean existsByEmailId(String emailId);

    Optional<EmployeeInformation> findByEmailId(String emailId);

    @Query("SELECT e FROM EmployeeInformation e WHERE e.name = :name")
    Optional<EmployeeInformation> findByName(@Param("name") String name);

    @Query("SELECT e.manager.managerId FROM EmployeeInformation e WHERE e.empId = :employeeId")
    String findReportingManagerIdByEmployeeId(@Param("employeeId") String employeeId);

    @Query("SELECT e.hR.hrId FROM EmployeeInformation e WHERE e.empId = :employeeId")
    Optional<String> findHrIdByEmployeeId(@Param("employeeId") String employeeId);

    @Query("SELECT e.name FROM EmployeeInformation e WHERE e.empId = :employeeId")
    Optional<String> findNameByEmpId(@Param("employeeId") String employeeId);

    Optional<EmployeeInformation> findByEmpId(String empId);

    @Query("SELECT e.empId FROM EmployeeInformation e")
    List<String> findAllEmployeeIds();
}
