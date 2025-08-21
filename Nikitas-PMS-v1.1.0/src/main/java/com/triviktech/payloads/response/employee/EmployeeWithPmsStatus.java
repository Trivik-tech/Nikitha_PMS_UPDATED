package com.triviktech.payloads.response.employee;

import com.triviktech.payloads.response.department.DepartmentResponseDto;

/**
 * DTO representing an employee along with their PMS (Performance Management System) status.
 * <p>
 * This object combines employee details with PMS progress indicators such as
 * self-review completion, manager review completion, PMS initiation status,
 * and KRA/KPI registration status.
 * </p>
 *
 * <h3>Fields:</h3>
 * <ul>
 *   <li>{@link #empId} - Unique Employee ID</li>
 *   <li>{@link #name} - Employee's full name</li>
 *   <li>{@link #officialEmailId} - Employee's official email address</li>
 *   <li>{@link #currentDesignation} - Employee's designation</li>
 *   <li>{@link #selfCompleted} - Whether the self-review is completed</li>
 *   <li>{@link #managerCompleted} - Whether the manager's review is completed</li>
 *   <li>{@link #pmsInitiated} - Whether PMS has been initiated for the employee</li>
 *   <li>{@link #kraKpiRegistered} - Whether the employee has registered KRA/KPIs</li>
 *   <li>{@link #department} - Department information (wrapped in DepartmentResponseDto)</li>
 * </ul>
 */

public class EmployeeWithPmsStatus {

    private String empId;

    private String name;
    private String officialEmailId;
    private String currentDesignation;

    private Boolean selfCompleted;
    private Boolean managerCompleted;
    private Boolean pmsInitiated;
    private DepartmentResponseDto department;
    private Boolean kraKpiRegistered;

    public Boolean getKraKpiRegistered() {
        return kraKpiRegistered;
    }

    public void setKraKpiRegistered(Boolean kraKpiRegistered) {
        this.kraKpiRegistered = kraKpiRegistered;
    }

    public DepartmentResponseDto getDepartment() {
        return department;
    }

    public void setDepartment(DepartmentResponseDto department) {
        this.department = department;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
