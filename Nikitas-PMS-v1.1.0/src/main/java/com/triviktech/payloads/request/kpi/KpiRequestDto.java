package com.triviktech.payloads.request.kpi;

/**
 * DTO (Data Transfer Object) class for capturing KPI (Key Performance Indicator) details
 * from the request payload. This class is mainly used in performance review or appraisal modules
 * where employees and managers provide KPI-related information.
 */

public class KpiRequestDto {
    private long kpiId;
    private String description;
    private Integer weightage;
    private Integer selfScore;
    private Integer managerScore;
    private Integer review2;
    private String employeeRemark;
    private String managerRemark;

    public Integer getReview2() {
        return review2;
    }
    

    public String getEmployeeRemark() {
        return employeeRemark;
    }


    public void setEmployeeRemark(String employeeRemark) {
        this.employeeRemark = employeeRemark;
    }


    public String getManagerRemark() {
        return managerRemark;
    }


    public void setManagerRemark(String managerRemark) {
        this.managerRemark = managerRemark;
    }


    public void setReview2(Integer review2) {
        this.review2 = review2;
    }

    public long getKpiId() {
        return kpiId;
    }

    public void setKpiId(long kpiId) {
        this.kpiId = kpiId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getWeightage() {
        return weightage;
    }

    public void setWeightage(Integer weightage) {
        this.weightage = weightage;
    }

    public Integer getSelfScore() {
        return selfScore;
    }

    public void setSelfScore(Integer selfScore) {
        this.selfScore = selfScore;
    }

    public Integer getManagerScore() {
        return managerScore;
    }

    public void setManagerScore(Integer managerScore) {
        this.managerScore = managerScore;
    }

    @Override
    public String toString() {
        return "KpiRequestDto{" +
                "kpiId=" + kpiId +
                ", description='" + description + '\'' +
                ", weightage=" + weightage +
                ", selfScore=" + selfScore +
                ", managerScore=" + managerScore +
                ", review2=" + review2 +
                '}';
    }
}
