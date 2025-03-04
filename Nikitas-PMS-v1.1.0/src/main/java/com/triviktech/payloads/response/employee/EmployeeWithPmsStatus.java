package com.triviktech.payloads.response.employee;

import com.triviktech.payloads.response.department.DepartmentResponseDto;

public class EmployeeWithPmsStatus {

    private String employeeId;

    private String firstName;
    private String lastName;
    private String email;
    private String designation;

    private DepartmentResponseDto department;

    private boolean selfCompleted;
    private boolean managerCompleted;
    private boolean pmsInitiated;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public boolean isSelfCompleted() {
        return selfCompleted;
    }

    public void setSelfCompleted(boolean selfCompleted) {
        this.selfCompleted = selfCompleted;
    }

    public boolean isManagerCompleted() {
        return managerCompleted;
    }

    public void setManagerCompleted(boolean managerCompleted) {
        this.managerCompleted = managerCompleted;
    }

    public boolean isPmsInitiated() {
        return pmsInitiated;
    }

    public void setPmsInitiated(boolean pmsInitiated) {
        this.pmsInitiated = pmsInitiated;
    }
}
