package com.triviktech.entities.kra;

import com.triviktech.entities.kpi.KPI;
import com.triviktech.entities.krakpi.KraKpi;
import jakarta.persistence.*;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Represents a Key Result Area (KRA).
 * <p>
 * A KRA defines a strategic objective or responsibility area within the organization.
 * It contains one or more Key Performance Indicators (KPIs) and can be associated with a
 * {@link KraKpi} entity that manages combined KRA/KPI assignments.
 */

@Entity
@Table(name = "kra")
public class KRA {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long kraId;

    @Column(name = "kra_name", nullable = false)
    private String kraName;


    @OneToMany(mappedBy = "kra", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<KPI> kpi;


    @Column(name = "weightage")
    private Integer weightage;

    @ManyToOne
    @JoinColumn(name = "kra_kpi_id")
    private KraKpi kraKpi;

    public KraKpi getKraKpi() {
        return kraKpi;
    }

    public void setKraKpi(KraKpi kraKpi) {
        this.kraKpi = kraKpi;
    }

    public Integer getWeightage() {
        return weightage;
    }

    public void setWeightage(Integer weightage) {
        this.weightage = weightage;
    }

    public Set<KPI> getKpi() {
        return kpi;
    }

    public void setKpi(Set<KPI> kpi) {
        this.kpi = kpi;
    }

    // Getters and Setters
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



}
