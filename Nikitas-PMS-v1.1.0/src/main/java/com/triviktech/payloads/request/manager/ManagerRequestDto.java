package com.triviktech.payloads.request.manager;

import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.Date;

public class ManagerRequestDto {

    @NotEmpty(message = "Manager id should bot be empty")
    private String managerId;

    @NotEmpty(message = "First Name Should not be empty")
    @Size(min = 3,message = "First Name should at least contains 3 characters")
    private String firstName;

    @NotEmpty(message = "Last Name Should not be empty")
    @Size(min = 3,message = "Last Name should at least contains 3 characters")
    private String lastName;

    @NotEmpty(message = "Email id should not be empty")
    private String email;

    private Long contactNumber;

    private Date dateOfBirth;


    @NotEmpty(message = "Role should not be empty")
    private String role;

    private long departmentId;

    private long stateId;

    private String password;


    @NotEmpty(message = "Location name should not be empty")
    private String locationName;

    @NotEmpty(message = "Zip code should not be empty")
    private String zipCode;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    private long projectId ;

    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
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

    public long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(long departmentId) {
        this.departmentId = departmentId;
    }


    public long getStateId() {
        return stateId;
    }

    public void setStateId(long stateId) {
        this.stateId = stateId;
    }



    public long getProjectId() {
        return projectId;
    }

    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }
}