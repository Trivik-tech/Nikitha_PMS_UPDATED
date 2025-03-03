package com.triviktech.payloads.request.krakpi;

import com.triviktech.payloads.request.kra.KraRequestDto;

import java.util.Set;

public class KraKpiRequestDto {
    private long kraKpiId;
    private String employeeId;
    private String remark;
    private Set<KraRequestDto> kra;


    private boolean selfCompleted ;  // Default value to avoid null

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
}
