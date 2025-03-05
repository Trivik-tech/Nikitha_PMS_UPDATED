package com.triviktech.payloads.response.kra;

import com.triviktech.payloads.response.employee.EmployeeInformationResponseDto;
import com.triviktech.payloads.response.kpi.KpiResponseDto;

import java.util.Set;

public class KraResponseDto1 {

    private Long kraId;

    private String kraName;



    private int weightage;

    Set<KpiResponseDto> kpi;

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



    public int getWeightage() {
        return weightage;
    }

    public void setWeightage(int weightage) {
        this.weightage = weightage;
    }

    public Set<KpiResponseDto> getKpi() {
        return kpi;
    }

    public void setKpi(Set<KpiResponseDto> kpi) {
        this.kpi = kpi;
    }
}
