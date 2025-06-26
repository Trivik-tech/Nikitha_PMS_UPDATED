package com.triviktech.entities.employee;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.triviktech.entities.department.Department;
import com.triviktech.entities.hr.HR;
import com.triviktech.entities.manager.Manager;
import jakarta.persistence.*;

import java.util.Date;


@Entity
@Table(name = "employee")
public class EmployeeInformation {

    @Id
    @Column(name = "employee_id")
    private String empId;

    @Column(name = "name")
    private String name;

    @Column(name = "mobile_number")
    private String mobileNumber;

    @Column(name = "date_of_joining")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date dateOfJoining;

    @Column(name = "current_designation")
    private String currentDesignation;


    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @Column(name = "role")
    private String role;

    @Column(name = "password", length = 500)
    private String password;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date dob;

    @Column(name = "branch")
    private String branch;

    @Column(name = "category")
    private String category;

    @Column(name = "last_working_day")
    private String lastWorkingDay;

    @Column(name = "official_email_id")
    private String officialEmailId;

    @Column(name = "email_id", length = 1000)
    private String emailId;

    @ManyToOne
    @JoinColumn(name = "manager_manager_id")
    private Manager manager;

    @ManyToOne
    @JoinColumn(name = "hr_id")
    private HR hR;

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public Date getDateOfJoining() {
        return dateOfJoining;
    }

    public void setDateOfJoining(Date dateOfJoining) {
        this.dateOfJoining = dateOfJoining;
    }

    public String getCurrentDesignation() {
        return currentDesignation;
    }

    public void setCurrentDesignation(String currentDesignation) {
        this.currentDesignation = currentDesignation;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
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

    public String getLastWorkingDay() {
        return lastWorkingDay;
    }

    public void setLastWorkingDay(String lastWorkingDay) {
        this.lastWorkingDay = lastWorkingDay;
    }

    public String getOfficialEmailId() {
        return officialEmailId;
    }

    public void setOfficialEmailId(String officialEmailId) {
        this.officialEmailId = officialEmailId;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public Manager getManager() {
        return manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }

    public HR gethR() {
        return hR;
    }

    public void sethR(HR hR) {
        this.hR = hR;
    }
}
