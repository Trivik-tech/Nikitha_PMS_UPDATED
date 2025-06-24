package com.triviktech.payloads.response.manager;

import com.triviktech.payloads.response.address.LocationResponseDto;
import com.triviktech.payloads.response.department.DepartmentResponseDto;
import com.triviktech.payloads.response.project.ProjectResponseDto;

import java.util.Date;
import java.util.Set;

public class ManagerResponseDto {

    private String managerId;

    private String firstName;

    private String lastName;

    private String email;

    private Long contactNumber;

    private Date dateOfBirth;

    private String role;

    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

    // Updated to reflect a single department
    private DepartmentResponseDto department;

   

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

    // Updated getter and setter for single department
    public DepartmentResponseDto getDepartment() {
        return department;
    }

    public void setDepartment(DepartmentResponseDto department) {
        this.department = department;
    }

   
}
