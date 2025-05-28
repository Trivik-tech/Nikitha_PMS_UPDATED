package com.triviktech.payloads.response.employee;

import com.triviktech.payloads.response.department.DepartmentResponseDto;

public class EmployeeWithPmsStatus {

    private String empId;

    private String name;
    private String officialEmailId;
    private String currentDesignation;

    private DepartmentResponseDto department;

    private Boolean selfCompleted;
    private Boolean managerCompleted;
    private Boolean pmsInitiated;

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

    public Boolean getSelfCompleted() {
        return selfCompleted;
    }

    public void setSelfCompleted(Boolean selfCompleted) {
        this.selfCompleted = selfCompleted;
    }

    public Boolean getManagerCompleted() {
        return managerCompleted;
    }

    public void setManagerCompleted(Boolean managerCompleted) {
        this.managerCompleted = managerCompleted;
    }

    public Boolean getPmsInitiated() {
        return pmsInitiated;
    }

    public void setPmsInitiated(Boolean pmsInitiated) {
        this.pmsInitiated = pmsInitiated;
    }
}
