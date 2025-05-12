package com.triviktech.entities.department;

import com.triviktech.entities.employee.EmployeeInformation;
import com.triviktech.entities.hr.HR;
import com.triviktech.entities.manager.Manager;
import jakarta.persistence.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "department")
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "department_id", nullable = false, updatable = false)
    private Long departmentId;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "department", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<EmployeeInformation> employees = new LinkedHashSet<>();

    @OneToMany(mappedBy = "department", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = false)
    private Set<Manager> managers = new LinkedHashSet<>();

    @ManyToOne
    @JoinColumn(name = "hr_id", nullable = true) // HR ID can be null during department creation
    private HR hr;

    // Getters and Setters
    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<EmployeeInformation> getEmployees() {
        return employees;
    }

    public void setEmployees(Set<EmployeeInformation> employees) {
        this.employees = employees;
    }

    public Set<Manager> getManagers() {
        return managers;
    }

    public void setManagers(Set<Manager> managers) {
        this.managers = managers;
    }

    public HR getHr() {
        return hr;
    }

    public void setHr(HR hr) {
        this.hr = hr;
    }


}
