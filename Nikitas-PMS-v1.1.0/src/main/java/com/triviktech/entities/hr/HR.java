package com.triviktech.entities.hr;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.triviktech.entities.department.Department;
import com.triviktech.entities.employee.EmployeeInformation;
import com.triviktech.entities.manager.Manager;
import jakarta.persistence.*;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Represents an HR (Human Resources) entity in the system.
 * <p>
 * This entity stores details of HR employees including personal info,
 * organizational details, login credentials, and relationships with
 * employees and managers.
 * <p>
 * Mapped to the "hr" table in the database.
 */

@Entity
@Table(name = "hr")
public class HR {
    @Id
    @Column(name = "hr_id", nullable = false, updatable = false)
    private String hrId;

    @Column(name = "first_name")
    private String name;

    @Column(name = "email", length = 500)
    private String emailId;

    @Column(name = "mobile_number")
    private String mobileNumber;

    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @Column(name = "dob")
    private Date dob;

    @Column(name = "role")
    private String role;

    @ManyToOne
    @JoinColumn(name = "department_id", nullable = true)
    private Department department;

    @Column(name = "password", length = 500)
    private String password;

    @Column(name = "current_designation")
    private String currentDesignation;

    @Temporal(TemporalType.DATE)
    @Column(name = "date_of_joining")
    private Date dateOfJoining;

    @Column(name = "branch")
    private String branch;

    @Column(name = "category")
    private String category;

    @Temporal(TemporalType.DATE)
    @Column(name = "last_working_date")
    private Date lastWorkingDate;

    @Column(name = "official_email_id", length = 500)
    private String officialEmailId;

    @Column(name = "reporting_manager")
    private String reportingManager;

    @OneToMany(mappedBy = "hR", orphanRemoval = true)
    private Set<EmployeeInformation> employeeInformations = new LinkedHashSet<>();

    @OneToMany(mappedBy = "hR", orphanRemoval = true)
    private Set<Manager> managers = new LinkedHashSet<>();

    public Set<Manager> getManagers() {
        return managers;
    }

    public void setManagers(Set<Manager> managers) {
        this.managers = managers;
    }

    public String getHrId() {
        return hrId;
    }

    public void setHrId(String hrId) {
        this.hrId = hrId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCurrentDesignation() {
        return currentDesignation;
    }

    public void setCurrentDesignation(String currentDesignation) {
        this.currentDesignation = currentDesignation;
    }

    public Date getDateOfJoining() {
        return dateOfJoining;
    }

    public void setDateOfJoining(Date dateOfJoining) {
        this.dateOfJoining = dateOfJoining;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Date getLastWorkingDate() {
        return lastWorkingDate;
    }

    public void setLastWorkingDate(Date lastWorkingDate) {
        this.lastWorkingDate = lastWorkingDate;
    }

    public String getOfficialEmailId() {
        return officialEmailId;
    }

    public void setOfficialEmailId(String officialEmailId) {
        this.officialEmailId = officialEmailId;
    }

    public String getReportingManager() {
        return reportingManager;
    }

    public void setReportingManager(String reportingManager) {
        this.reportingManager = reportingManager;
    }

    public Set<EmployeeInformation> getEmployeeInformations() {
        return employeeInformations;
    }

    public void setEmployeeInformations(Set<EmployeeInformation> employeeInformations) {
        this.employeeInformations = employeeInformations;
    }
}
