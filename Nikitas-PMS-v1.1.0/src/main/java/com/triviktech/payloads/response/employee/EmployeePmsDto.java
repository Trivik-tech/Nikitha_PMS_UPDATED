package com.triviktech.payloads.response.employee;

import com.triviktech.payloads.response.krakpi.KraKpiDto;

import java.util.List;

public class EmployeePmsDto {
    private String empId;
    private String name;
    private String department;
    private String designation;
    private String photoPath;

    private String dueDate;
    private String employeeReviewDate;
    private String managerReviewDate;

    private List<KraKpiDto> kraKpiDetails;

    private double selfScore;
    private double managerScore;
    private double finalScore;
    private String overallRemark;

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

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getEmployeeReviewDate() {
        return employeeReviewDate;
    }

    public void setEmployeeReviewDate(String employeeReviewDate) {
        this.employeeReviewDate = employeeReviewDate;
    }

    public String getManagerReviewDate() {
        return managerReviewDate;
    }

    public void setManagerReviewDate(String managerReviewDate) {
        this.managerReviewDate = managerReviewDate;
    }

    public List<KraKpiDto> getKraKpiDetails() {
        return kraKpiDetails;
    }

    public void setKraKpiDetails(List<KraKpiDto> kraKpiDetails) {
        this.kraKpiDetails = kraKpiDetails;
    }

    public double getSelfScore() {
        return selfScore;
    }

    public void setSelfScore(double selfScore) {
        this.selfScore = selfScore;
    }

    public double getManagerScore() {
        return managerScore;
    }

    public void setManagerScore(double managerScore) {
        this.managerScore = managerScore;
    }

    public double getFinalScore() {
        return finalScore;
    }

    public void setFinalScore(double finalScore) {
        this.finalScore = finalScore;
    }

    public String getOverallRemark() {
        return overallRemark;
    }

    public void setOverallRemark(String overallRemark) {
        this.overallRemark = overallRemark;
    }
}
