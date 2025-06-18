package com.triviktech.repositories.manager;

import com.triviktech.entities.manager.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ManagerRepository extends JpaRepository<Manager, String> {
    Optional<Manager> findByManagerId(String managerId);
    boolean existsByEmailId(String emailId);
    Optional<Manager> findByEmailId(String emailId);

    @Query("SELECT m FROM Manager m " +
            "LEFT JOIN m.department d " +
            "WHERE LOWER(m.managerId) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(m.name) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(d.name) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(m.role) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(m.category) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<Manager> searchManagers(@Param("search") String search);

    Optional<Manager> findByName(String rmName);

    // Fixed return type from Arrays to List<Manager>
    List<Manager> findByManagerIdIn(Set<String> empIds);

    // Fixed: add JPQL query to count managers grouped by department
    @Query("SELECT m.department.name, COUNT(m) FROM Manager m GROUP BY m.department.name")
    List<Object[]> countManagersByDepartment();
}