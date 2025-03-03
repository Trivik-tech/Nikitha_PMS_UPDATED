package com.triviktech.payloads.response.hr;

import com.triviktech.payloads.response.address.LocationResponseDto;
import com.triviktech.payloads.response.department.DepartmentResponseDto;
import com.triviktech.payloads.response.manager.ManagerResponseDto;
import com.triviktech.payloads.response.project.ProjectResponseDto;

import java.util.Set;

public class HrResponseDto {

    private String hrId;
    private String firstName;
    private String lastName;
    private String email;
    private String role;
    private long contactNumber;
    private String dateOfBirth;;
    private LocationResponseDto locationResponseDto;
    private Set<DepartmentResponseDto> departments;
    private Set<ProjectResponseDto> projects;
    private Set<ManagerResponseDto> managers;

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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public long getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(long contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public LocationResponseDto getLocationResponseDto() {
        return locationResponseDto;
    }

    public void setLocationResponseDto(LocationResponseDto locationResponseDto) {
        this.locationResponseDto = locationResponseDto;
    }

    public Set<DepartmentResponseDto> getDepartments() {
        return departments;
    }

    public void setDepartments(Set<DepartmentResponseDto> departments) {
        this.departments = departments;
    }

    public Set<ProjectResponseDto> getProjects() {
        return projects;
    }

    public void setProjects(Set<ProjectResponseDto> projects) {
        this.projects = projects;
    }

    public Set<ManagerResponseDto> getManagers() {
        return managers;
    }

    public void setManagers(Set<ManagerResponseDto> managers) {
        this.managers = managers;
    }
}
