package com.triviktech.payloads.response.employee;

import com.triviktech.payloads.response.department.DepartmentResponseDto;

public class EmployeeWithPmsStatus {

    private String empId;

    private String name;
    private String officialEmailId;
    private String currentDesignation;

    private DepartmentResponseDto department;

    private boolean selfCompleted;
    private boolean managerCompleted;
    private boolean pmsInitiated;

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

    public String getOfficialEmailId() {
        return officialEmailId;
    }

    public void setOfficialEmailId(String officialEmailId) {
        this.officialEmailId = officialEmailId;
    }

    public String getCurrentDesignation() {
        return currentDesignation;
    }

    public void setCurrentDesignation(String currentDesignation) {
        this.currentDesignation = currentDesignation;
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
