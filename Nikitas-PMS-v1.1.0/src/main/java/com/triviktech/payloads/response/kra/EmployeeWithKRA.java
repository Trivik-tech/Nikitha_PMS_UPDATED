package com.triviktech.payloads.response.kra;

import com.triviktech.payloads.response.department.DepartmentResponseDto;

import java.util.List;

/**
 * Data Transfer Object (DTO) representing an Employee along with their
 * assigned Key Result Areas (KRAs).
 * <p>
 * This DTO aggregates basic employee details, department information,
 * and the list of KRAs (and their associated KPIs).
 * </p>
 *
 * <ul>
 *   <li>{@link #employeeId} - Unique identifier of the employee</li>
 *   <li>{@link #firstName} - First name of the employee</li>
 *   <li>{@link #lastName} - Last name of the employee</li>
 *   <li>{@link #department} - Department to which the employee belongs</li>
 *   <li>{@link #assessmentPeriod} - Period of performance assessment</li>
 *   <li>{@link #summaryDate} - Date when the summary was created</li>
 *   <li>{@link #kra} - List of KRAs with associated KPIs</li>
 * </ul>
 */

public class EmployeeWithKRA {

    private String employeeId;
    private String firstName;
    private String lastName;
    private DepartmentResponseDto department;
    private String assessmentPeriod;
    private String summaryDate;

    List<KraResponseDto1> kra;

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

    public DepartmentResponseDto getDepartment() {
        return department;
    }

    public void setDepartment(DepartmentResponseDto department) {
        this.department = department;
    }

    public String getAssessmentPeriod() {
        return assessmentPeriod;
    }

    public void setAssessmentPeriod(String assessmentPeriod) {
        this.assessmentPeriod = assessmentPeriod;
    }

    public String getSummaryDate() {
        return summaryDate;
    }

    public void setSummaryDate(String summaryDate) {
        this.summaryDate = summaryDate;
    }

    public List<KraResponseDto1> getKra() {
        return kra;
    }

    public void setKra(List<KraResponseDto1> kra) {
        this.kra = kra;
    }
}
