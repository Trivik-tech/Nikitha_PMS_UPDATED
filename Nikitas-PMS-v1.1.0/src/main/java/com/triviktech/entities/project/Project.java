package com.triviktech.entities.project;

import com.triviktech.entities.employee.EmployeeInformation;
import com.triviktech.entities.hr.HR;
import com.triviktech.entities.manager.Manager;
import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "project")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id", nullable = false)
    private Long projectId;

    @Column(name = "name")
    private String name;

    @ManyToMany(mappedBy = "projects")
    private Set<EmployeeInformation> employees ;

    @ManyToMany(mappedBy = "projects")
    private Set<Manager> managers ;

    @ManyToMany(mappedBy = "projects")
    private Set<HR> hr;

    public Set<HR> getHr() {
        return hr;
    }

    public void setHr(Set<HR> hr) {
        this.hr = hr;
    }

    public Set<Manager> getManagers() {
        return managers;
    }

    public void setManagers(Set<Manager> managers) {
        this.managers = managers;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
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


}