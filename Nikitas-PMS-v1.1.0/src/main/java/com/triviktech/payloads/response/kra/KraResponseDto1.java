package com.triviktech.payloads.response.kra;

import com.triviktech.payloads.response.kpi.KpiResponseDto;

import java.util.Set;

/**
 * Data Transfer Object (DTO) representing a Key Result Area (KRA) response.
 * <p>
 * A KRA is a major responsibility area for an employee, and each KRA
 * can be associated with multiple {@link KpiResponseDto Key Performance Indicators (KPIs)}.
 * </p>
 *
 * <ul>
 *   <li>{@link #kraId} - Unique identifier for the KRA</li>
 *   <li>{@link #kraName} - Name or title of the KRA</li>
 *   <li>{@link #weightage} - Importance or weight of this KRA in percentage</li>
 *   <li>{@link #kpi} - Set of KPIs associated with this KRA</li>
 * </ul>
 */

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
