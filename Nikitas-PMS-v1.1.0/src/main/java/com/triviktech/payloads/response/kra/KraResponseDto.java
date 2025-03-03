package com.triviktech.payloads.response.kra;

import com.triviktech.payloads.response.employee.EmployeeInformationResponseDto;

public class KraResponseDto {

    private Long kraId;


    private String kraName;

    private String description;

    private Integer selfScore;


    private Integer managerScore;


    private boolean selfCompleted;

    private boolean managerCompleted;

    private String remark;

    private EmployeeInformationResponseDto employee;


    public Long getKraId() {
        return kraId;
    }

    public void setKraId(Long kraId) {
        this.kraId = kraId;
    }

    public String getKraName() {
        return kraName;
    }

    public void setKraName(String kraName) {
        this.kraName = kraName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public EmployeeInformationResponseDto getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeeInformationResponseDto employee) {
        this.employee = employee;
    }
}
