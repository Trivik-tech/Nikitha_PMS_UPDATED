package com.triviktech.payloads.request.employee;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.Date;

public class Employee {

    @NotEmpty(message = "Employee Id Should not be empty")
    private String empId;

    @NotEmpty(message = "Employee name should not be empty")
    @Size(min = 3,message = "Employee name must at least contains 3 Characters")
    private String name;
    private Date dob;
    private Date dateOfJoining;

    @NotEmpty(message = "Designation Should not be empty")
    @Size(min=3,message = "Employee Designation must at least contains 3 Characters")
    private String currentDesignation;

    @NotEmpty(message = "Department should not be empty")
    @Size(min = 3 , message = "Department must at least contains 3 Characters")
    private String department;

    @NotEmpty(message = "Branch should not be empty")
    @Size(min = 3 , message = "Branch must at least contains 3 Characters")
    private String branch;

    @NotEmpty(message = "Category should not be empty")
    @Size(min = 3 , message = "Category must at least contains 3 Characters")
    private String category;

    private Date lastWorkingDate;

    @NotEmpty(message = "Official Email Id should not be empty")
    private String officialEmailId;

    @NotEmpty(message = "Mobile Number should not be empty")
    private String mobileNumber;

    @NotEmpty(message = "Reporting Manager should not be empty")
    @Size(min = 3 , message = "Reporting Manager must at least contains 3 Characters")
    private String reportingManager;

    @NotEmpty(message = "Email Id should not be empty")
    private String emailId;

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

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
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

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
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

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getReportingManager() {
        return reportingManager;
    }

    public void setReportingManager(String reportingManager) {
        this.reportingManager = reportingManager;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "empId='" + empId + '\'' +
                ", name='" + name + '\'' +
                ", dob=" + dob +
                ", dateOfJoining=" + dateOfJoining +
                ", currentDesignation='" + currentDesignation + '\'' +
                ", department='" + department + '\'' +
                ", branch='" + branch + '\'' +
                ", category='" + category + '\'' +
                ", lastWorkingDate=" + lastWorkingDate +
                ", officialEmailId='" + officialEmailId + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", reportingManager='" + reportingManager + '\'' +
                ", emailId='" + emailId + '\'' +
                '}';
    }
}
