package com.triviktech.payloads.response.krakpi;

import com.triviktech.payloads.response.employee.EmployeeResponseDto;

import com.triviktech.payloads.response.kra.KraResponseDto1;

import java.util.Set;

public class KraKpiResponseDto {

    private long id;
    private String remark;

    private EmployeeResponseDto employee;

    private Set<KraResponseDto1> kra;

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

    public EmployeeResponseDto getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeeResponseDto employee) {
        this.employee = employee;
    }

    public Set<KraResponseDto1> getKra() {
        return kra;
    }

    public void setKra(Set<KraResponseDto1> kra) {
        this.kra = kra;
    }
}
