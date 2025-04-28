package com.triviktech.payloads.request.kra;

import com.triviktech.payloads.request.kpi.KpiRequestDto;

import java.util.Set;

public class KraRequestDto {
    private long kraId;
    private String kraName;
    private Integer weightage;
    private Set<KpiRequestDto> kpi;

    public long getKraId() {
        return kraId;
    }

    public void setKraId(long kraId) {
        this.kraId = kraId;
    }

    public String getKraName() {
        return kraName;
    }

    public void setKraName(String kraName) {
        this.kraName = kraName;
    }


    public Integer getWeightage() {
        return weightage;
    }

    public void setWeightage(Integer weightage) {
        this.weightage = weightage;
    }

    public Set<KpiRequestDto> getKpi() {
        return kpi;
    }

    public void setKpi(Set<KpiRequestDto> kpi) {
        this.kpi = kpi;
    }

    @Override
    public String toString() {
        return "KraRequestDto{" +
                "kraId=" + kraId +
                ", kraName='" + kraName + '\'' +
                ", weightage=" + weightage +
                ", kpi=" + kpi +
                '}';
    }
}
