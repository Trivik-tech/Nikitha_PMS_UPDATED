package com.triviktech.payloads.request.krakpi;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.triviktech.payloads.request.kra.KraRequestDto;

import java.util.Date;
import java.util.Set;

public class KraKpiRequestDto {
    private long kraKpiId;
    private String employeeId;
    private String remark;
    private Set<KraRequestDto> kra;
    private boolean review2;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date dueDate;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date selfReviewDate;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date managerReviewDate;
    private Boolean pmsInitiated;
    private Boolean managerApproval;
    private Boolean selfCompleted ;
    private boolean managerCompleted;


    public long getKraKpiId() {
        return kraKpiId;
    }

    public void setKraKpiId(long kraKpiId) {
        this.kraKpiId = kraKpiId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Set<KraRequestDto> getKra() {
        return kra;
    }

    public void setKra(Set<KraRequestDto> kra) {
        this.kra = kra;
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

    public Boolean getManagerApproval() {
        return managerApproval;
    }

    public void setManagerApproval(Boolean managerApproval) {
        this.managerApproval = managerApproval;
    }

    public Boolean getSelfCompleted() {
        return selfCompleted;
    }

    public void setSelfCompleted(Boolean selfCompleted) {
        this.selfCompleted = selfCompleted;
    }

    public boolean isManagerCompleted() {
        return managerCompleted;
    }

    public void setManagerCompleted(boolean managerCompleted) {
        this.managerCompleted = managerCompleted;
    }

    @Override
    public String toString() {
        return "KraKpiRequestDto{" +
                "kraKpiId=" + kraKpiId +
                ", employeeId='" + employeeId + '\'' +
                ", remark='" + remark + '\'' +
                ", kra=" + kra +
                ", review2=" + review2 +
                ", dueDate=" + dueDate +
                ", selfReviewDate=" + selfReviewDate +
                ", managerReviewDate=" + managerReviewDate +
                ", pmsInitiated=" + pmsInitiated +
                ", managerApproval=" + managerApproval +
                ", selfCompleted=" + selfCompleted +
                ", managerCompleted=" + managerCompleted +
                '}';
    }
}
