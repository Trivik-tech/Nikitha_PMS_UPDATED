package com.triviktech.payloads.response.employee;

import com.triviktech.payloads.response.department.DepartmentResponseDto;
import com.triviktech.payloads.response.manager.ManagerResponseDto;
import com.triviktech.payloads.response.project.ProjectResponseDto;

import java.util.Date;
import java.util.Set;


public class EmployeeInformationResponseDto {

    private String employeeId;

    private String firstName;
    private String lastName;


    private Long contactNumber;

    private Date dateOfJoining;

    private String designation;

    private DepartmentResponseDto department;

    private ManagerResponseDto manager;

    private String role;

    private Set<ProjectResponseDto> projects;

    public Set<ProjectResponseDto> getProjects() {
        return projects;
    }

    public void setProjects(Set<ProjectResponseDto> projects) {
        this.projects = projects;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
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

    public Long getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(Long contactNumber) {
        this.contactNumber = contactNumber;
    }

    public Date getDateOfJoining() {
        return dateOfJoining;
    }

    public void setDateOfJoining(Date dateOfJoining) {
        this.dateOfJoining = dateOfJoining;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public DepartmentResponseDto getDepartment() {
        return department;
    }

    public void setDepartment(DepartmentResponseDto department) {
        this.department = department;
    }

    public ManagerResponseDto getManager() {
        return manager;
    }

    public void setManager(ManagerResponseDto manager) {
        this.manager = manager;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}