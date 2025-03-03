package com.triviktech.payloads.response.kra;

import com.triviktech.payloads.response.department.DepartmentResponseDto;

import java.util.List;

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
