package com.triviktech.payloads.response.krakpi;

import com.triviktech.payloads.response.employee.EmployeeInfo;


import com.triviktech.payloads.response.kra.KraResponseDto1;

import java.util.Date;
import java.util.Set;

/**
 * DTO representing the combined response of KRA (Key Result Areas) and KPI (Key Performance Indicators).
 * This class captures the performance review details of an employee,
 * including review dates, approval status, completion status, and associated remarks.
 *
 * Fields:
 * <ul>
 *   <li>{@code id} - Unique identifier for the KRA/KPI record</li>
 *   <li>{@code remark} - Remarks or comments provided during the review</li>
 *   <li>{@link EmployeeInfo employee} - Employee details associated with this KRA/KPI</li>
 *   <li>{@link KraResponseDto1 kra} - Set of KRAs assigned to the employee</li>
 *   <li>{@code review2} - Indicates if a second review is required</li>
 *   <li>{@code dueDate} - Due date for completing the review process</li>
 *   <li>{@code selfReviewDate} - Date when the employee completed the self-review</li>
 *   <li>{@code managerReviewDate} - Date when the manager completed their review</li>
 *   <li>{@code pmsInitiated} - Indicates whether the PMS (Performance Management System) process is initiated</li>
 *   <li>{@code managerApproval} - Indicates if the manager has approved the review</li>
 *   <li>{@code selfCompleted} - Indicates if the self-review process is completed</li>
 *   <li>{@code managerCompleted} - Indicates if the manager review process is completed</li>
 * </ul>
 */

public class KraKpiResponseDto {

    private long id;
    private String remark;

    private EmployeeInfo employee;

    private Set<KraResponseDto1> kra;

    private boolean review2;
    private Date dueDate;
    private Date selfReviewDate;
    private Date managerReviewDate;
    private Boolean pmsInitiated;
    private Boolean managerApproval;
    private boolean selfCompleted;

    private boolean managerCompleted;

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

    public Boolean getManagerApproval() {
        return managerApproval;
    }

    public void setManagerApproval(Boolean managerApproval) {
        this.managerApproval = managerApproval;
    }

    public boolean isReview2() {
        return review2;
    }

    public void setReview2(boolean review2) {
        this.review2 = review2;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getSelfReviewDate() {
        return selfReviewDate;
    }

    public void setSelfReviewDate(Date selfReviewDate) {
        this.selfReviewDate = selfReviewDate;
    }

    public Date getManagerReviewDate() {
        return managerReviewDate;
    }

    public void setManagerReviewDate(Date managerReviewDate) {
        this.managerReviewDate = managerReviewDate;
    }

    public Boolean getPmsInitiated() {
        return pmsInitiated;
    }

    public void setPmsInitiated(Boolean pmsInitiated) {
        this.pmsInitiated = pmsInitiated;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public EmployeeInfo getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeeInfo employee) {
        this.employee = employee;
    }

    public Set<KraResponseDto1> getKra() {
        return kra;
    }

    public void setKra(Set<KraResponseDto1> kra) {
        this.kra = kra;
    }
}
