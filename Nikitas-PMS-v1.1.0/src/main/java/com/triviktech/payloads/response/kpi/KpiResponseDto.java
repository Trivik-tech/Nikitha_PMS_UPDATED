package com.triviktech.payloads.response.kpi;

/**
 * Represents the response details of a KPI (Key Performance Indicator).
 * <p>
 * This DTO (Data Transfer Object) is used in the PMS system to hold
 * KPI-related details such as description, weightage, self/manager scores,
 * average scores, and remarks.
 * </p>
 *
 * <h2>Fields:</h2>
 * <ul>
 *   <li>{@link #id} - Unique identifier of the KPI.</li>
 *   <li>{@link #description} - Description of the KPI.</li>
 *   <li>{@link #weightage} - Weightage assigned to the KPI.</li>
 *   <li>{@link #selfScore} - Score given by the employee (self-assessment).</li>
 *   <li>{@link #managerScore} - Score given by the manager.</li>
 *   <li>{@link #average} - Average score computed based on self and manager ratings.</li>
 *   <li>{@link #review2} - Secondary review score (e.g., Review-2).</li>
 *   <li>{@link #employeeRemark} - Remarks provided by the employee.</li>
 *   <li>{@link #managerRemark} - Remarks provided by the manager.</li>
 * </ul>
 */

public class KpiResponseDto {

    private long id;
    private String description;
    private Integer weightage;
    private Integer selfScore;
    private Integer managerScore;
    private Float average;
    private Integer review2;
    private String employeeRemark;
    private String managerRemark;

    public Float getAverage() {
        return average;
    }

    public void setAverage(Float average) {
        this.average = average;
    }

    public Integer getReview2() {
        return review2;
    }

    public void setReview2(Integer review2) {
        this.review2 = review2;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
}
