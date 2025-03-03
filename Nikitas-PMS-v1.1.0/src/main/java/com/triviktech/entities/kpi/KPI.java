package com.triviktech.entities.kpi;

import com.triviktech.entities.kra.KRA;
import jakarta.persistence.*;


@Entity
@Table(name = "kpi")
public class KPI {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "kpi_id", nullable = false)
    private Long kpiId;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "weightage")
    private Integer weightage;

    @Column(name = "self_score")
    private Integer selfScore;

    @Column(name = "manager_score")
    private Integer managerScore;

    @ManyToOne
    @JoinColumn(name = "kra_id")
    private KRA kra;

    public Long getKpiId() {
        return kpiId;
    }

    public void setKpiId(Long kpiId) {
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

    public KRA getKra() {
        return kra;
    }

    public void setKra(KRA kra) {
        this.kra = kra;
    }
}