package com.triviktech.repositories.manager;

import com.triviktech.entities.manager.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Repository interface for performing CRUD and custom query operations
 * on the {@link Manager} entity.
 *
 * <p><b>Entity Relationships:</b></p>
 * <ul>
 *   <li>{@link Manager} → {@link com.triviktech.entities.department.Department} (Many-to-One)</li>
 *   <li>Manager has fields like: managerId, name, emailId, role, category</li>
 * </ul>
 *
 * <p>This repository provides:</p>
 * <ul>
 *   <li>Find manager by ID, name, or email.</li>
 *   <li>Search managers with partial match on multiple fields.</li>
 *   <li>Count managers grouped by department.</li>
 *   <li>Bulk fetch using manager IDs.</li>
 * </ul>
 */
public interface ManagerRepository extends JpaRepository<Manager, String> {

    /**
     * Finds a manager by their unique manager ID.
     *
     * @param managerId the ID of the manager
     * @return an {@link Optional} containing the manager if found, otherwise empty
     */
    Optional<Manager> findByManagerId(String managerId);

    /**
     * Checks if a manager exists with the given email ID.
     *
     * @param emailId the email ID of the manager
     * @return {@code true} if a manager exists, otherwise {@code false}
     */
    boolean existsByEmailId(String emailId);

    /**
     * Finds a manager by their email ID.
     *
     * @param emailId the email ID of the manager
     * @return an {@link Optional} containing the manager if found, otherwise empty
     */
    Optional<Manager> findByEmailId(String emailId);

    /**
     * Searches managers by partial match on manager ID, name, department name,
     * role, or category (case-insensitive).
     *
     * @param search the search keyword
     * @return a list of {@link Manager} entities matching the search criteria
     */
    @Query("SELECT m FROM Manager m " +
            "LEFT JOIN m.department d " +
            "WHERE LOWER(m.managerId) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(m.name) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(d.name) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(m.role) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(m.category) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<Manager> searchManagers(@Param("search") String search);

    /**
     * Finds a manager by their name.
     *
     * @param rmName the name of the manager
     * @return an {@link Optional} containing the manager if found, otherwise empty
     */
    Optional<Manager> findByName(String rmName);

    /**
     * Finds all managers whose IDs are in the given set.
     *
     * @param empIds a set of manager IDs
     * @return a list of {@link Manager} entities
     */
    List<Manager> findByManagerIdIn(Set<String> empIds);

    /**
     * Counts the number of managers grouped by department.
     *
     * @return a list of object arrays where index 0 is the department name
     *         and index 1 is the count of managers
     */
    @Query("SELECT m.department.name, COUNT(m) FROM Manager m GROUP BY m.department.name")
    List<Object[]> countManagersByDepartment();
}
