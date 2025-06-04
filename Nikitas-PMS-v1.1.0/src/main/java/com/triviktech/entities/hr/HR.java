package com.triviktech.entities.hr;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.triviktech.entities.address.Location;
import com.triviktech.entities.department.Department;
import com.triviktech.entities.project.Project;
import com.triviktech.entities.manager.Manager; // Import Manager entity
import jakarta.persistence.*;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "hr")
public class HR {
    @Id
    @Column(name = "hr_id", nullable = false, updatable = false)
    private String hrId;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email", nullable = false, unique = true, length = 500)
    private String email; 

    @Column(name = "contact_number", nullable = false)
    private Long contactNumber;

    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @Column(name = "date_of_birth", nullable = false)
    private Date dateOfBirth;

    @Column(name = "role", nullable = false)
    private String role;

    @OneToOne(orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "location_id")
    private Location location;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "hr_project",
            joinColumns = @JoinColumn(name = "hr_id"),
            inverseJoinColumns = @JoinColumn(name = "project_id")
    )
    private Set<Project> projects = new LinkedHashSet<>();

    @OneToMany(mappedBy = "hr", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Department> departments = new LinkedHashSet<>();

    @ManyToOne
    @JoinColumn(name = "reporting_manager_id") // Foreign key column for the manager
    private Manager reportingManager;

    @Column(name = "password", nullable = false, length = 500)
    private String password;

    // Getters and Setters
    public String getHrId() {
        return hrId;
    }

    public void setHrId(String hrId) {
        this.hrId = hrId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(Long contactNumber) {
        this.contactNumber = contactNumber;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Set<Project> getProjects() {
        return projects;
    }

    public void setProjects(Set<Project> projects) {
        this.projects = projects;
    }

    public Set<Department> getDepartments() {
        return departments;
    }

    public void setDepartments(Set<Department> departments) {
        this.departments = departments;
    }

    public Manager getReportingManager() {
        return reportingManager;
    }

    public void setReportingManager(Manager reportingManager) {
        this.reportingManager = reportingManager;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
