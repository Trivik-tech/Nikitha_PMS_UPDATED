package com.triviktech.entities.manager;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.triviktech.entities.address.Location;
import com.triviktech.entities.department.Department;
import com.triviktech.entities.hr.HR;
import com.triviktech.entities.project.Project;
import com.triviktech.entities.employee.EmployeeInformation; // Import EmployeeInformation entity
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "manager")
public class Manager {

    @Id
    @Column(name = "manager_id")
    private String managerId;

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
    @JoinColumn(name = "location_id")
    private Location location;

    @ManyToOne
    @JoinColumn(name = "department_id", nullable = true)
    private Department department;

    @ManyToMany
    private Set<Project> projects;



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

    @OneToMany(mappedBy = "manager", orphanRemoval = true)
    private Set<EmployeeInformation> employeeInformations = new LinkedHashSet<>();

    @ManyToOne
    @JoinColumn(name = "hr_id")
    private HR hR;

    public HR getHR() {
        return hR;
    }

    public void setHR(HR hR) {
        this.hR = hR;
    }

    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
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

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Set<Project> getProjects() {
        return projects;
    }

    public void setProjects(Set<Project> projects) {
        this.projects = projects;
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

    public HR gethR() {
        return hR;
    }

    public void sethR(HR hR) {
        this.hR = hR;
    }
}
