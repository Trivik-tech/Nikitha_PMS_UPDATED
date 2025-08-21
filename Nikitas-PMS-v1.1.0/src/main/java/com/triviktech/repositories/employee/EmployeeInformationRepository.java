package com.triviktech.repositories.employee;

import com.triviktech.entities.employee.EmployeeInformation;
import com.triviktech.entities.manager.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Repository interface for performing CRUD and custom queries
 * on {@link EmployeeInformation} entities.
 * <p>
 * Extends {@link JpaRepository} to provide built-in persistence methods
 * such as save, findById, findAll, and delete.
 */
public interface EmployeeInformationRepository extends JpaRepository<EmployeeInformation, String> {

    /**
     * Searches employees by matching employee ID, name, department name,
     * manager name, role, or category with the given search term.
     *
     * @param search the search keyword (case-insensitive, partial match supported)
     * @return a list of {@link EmployeeInformation} matching the search criteria
     */
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

    /**
     * Finds employees whose IDs are present in the provided set.
     *
     * @param empIds the set of employee IDs
     * @return a list of matching {@link EmployeeInformation}
     */
    List<EmployeeInformation> findByEmpIdIn(Set<String> empIds);

    /**
     * Finds an employee by their manager and employee ID.
     *
     * @param manager    the {@link Manager} supervising the employee
     * @param employeeId the employee ID
     * @return an {@link Optional} containing the employee if found
     */
    Optional<EmployeeInformation> findByManagerAndEmpId(Manager manager, String employeeId);

    /**
     * Counts the number of employees grouped by department.
     *
     * @return a list of objects, where each element contains:
     *         - department name ({@link String})
     *         - employee count ({@link Long})
     */
    @Query("SELECT e.department.name, COUNT(e) FROM EmployeeInformation e GROUP BY e.department.name")
    List<Object[]> countEmployeesByDepartment();

    /**
     * Finds all employees managed by the given manager.
     *
     * @param manager the {@link Manager}
     * @return a list of {@link EmployeeInformation} reporting to the manager
     */
    List<EmployeeInformation> findAllByManager(Manager manager);

    /**
     * Checks if an employee exists with the given email ID.
     *
     * @param emailId the email ID
     * @return true if an employee exists with this email ID, false otherwise
     */
    boolean existsByEmailId(String emailId);

    /**
     * Finds an employee by their email ID.
     *
     * @param emailId the email ID
     * @return an {@link Optional} containing the employee if found
     */
    Optional<EmployeeInformation> findByEmailId(String emailId);

    /**
     * Finds an employee by their exact name.
     *
     * @param name the employee name
     * @return an {@link Optional} containing the employee if found
     */
    @Query("SELECT e FROM EmployeeInformation e WHERE e.name = :name")
    Optional<EmployeeInformation> findByName(@Param("name") String name);

    /**
     * Retrieves the manager ID of the reporting manager for a given employee.
     *
     * @param employeeId the employee ID
     * @return the manager ID as a {@link String}
     */
    @Query("SELECT e.manager.managerId FROM EmployeeInformation e WHERE e.empId = :employeeId")
    String findReportingManagerIdByEmployeeId(@Param("employeeId") String employeeId);

    /**
     * Retrieves the HR ID assigned to the given employee.
     *
     * @param employeeId the employee ID
     * @return an {@link Optional} containing the HR ID if available
     */
    @Query("SELECT e.hR.hrId FROM EmployeeInformation e WHERE e.empId = :employeeId")
    Optional<String> findHrIdByEmployeeId(@Param("employeeId") String employeeId);

    /**
     * Finds the employee name for a given employee ID.
     *
     * @param employeeId the employee ID
     * @return an {@link Optional} containing the employee name
     */
    @Query("SELECT e.name FROM EmployeeInformation e WHERE e.empId = :employeeId")
    Optional<String> findNameByEmpId(@Param("employeeId") String employeeId);
}
