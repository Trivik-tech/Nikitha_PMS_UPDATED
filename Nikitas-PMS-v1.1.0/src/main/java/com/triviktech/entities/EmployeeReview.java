package com.triviktech.entities;

import com.triviktech.entities.kpi.KPI;
import com.triviktech.entities.kra.KRA;
import com.triviktech.entities.krakpi.KraKpi;
import jakarta.persistence.*;

@Entity
@Table(name = "employee_review")
public class EmployeeReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Link to the employee’s KraKpi record */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kra_kpi_id", nullable = false)
    private KraKpi kraKpi;

    /** Link to the specific KRA */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kra_id", nullable = false)
    private KRA kra;

    /** Link to the specific KPI under that KRA */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kpi_id", nullable = false)
    private KPI kpi;

    /** Self review fields (employee inputs) */
    @Column(name = "self_score")
    private Integer selfScore;

    @Column(name = "employee_remark", length = 1000)
    private String employeeRemark;

    /** Manager review fields (manager inputs) */
    @Column(name = "manager_score")
    private Integer managerScore;

    @Column(name = "manager_remark", length = 1000)
    private String managerRemark;

    /** Optional 2nd level review (if applicable) */
    @Column(name = "review2_score")
    private Integer review2Score;

    /** Computed average of self/manager/review2 scores */
    @Column(name = "average_score")
    private Float averageScore;

    // ---------- Getters and Setters ----------

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public KraKpi getKraKpi() {
        return kraKpi;
    }

    public void setKraKpi(KraKpi kraKpi) {
        this.kraKpi = kraKpi;
    }

    public KRA getKra() {
        return kra;
    }

    public void setKra(KRA kra) {
        this.kra = kra;
    }

    public KPI getKpi() {
        return kpi;
    }

    public void setKpi(KPI kpi) {
        this.kpi = kpi;
    }

    public Integer getSelfScore() {
        return selfScore;
    }

    public void setSelfScore(Integer selfScore) {
        this.selfScore = selfScore;
    }

    public String getEmployeeRemark() {
        return employeeRemark;
    }

    public void setEmployeeRemark(String employeeRemark) {
        this.employeeRemark = employeeRemark;
    }

    public Integer getManagerScore() {
        return managerScore;
    }

    public void setManagerScore(Integer managerScore) {
        this.managerScore = managerScore;
    }

    public String getManagerRemark() {
        return managerRemark;
    }

    public void setManagerRemark(String managerRemark) {
        this.managerRemark = managerRemark;
    }

    public Integer getReview2Score() {
        return review2Score;
    }

    public void setReview2Score(Integer review2Score) {
        this.review2Score = review2Score;
    }

    public Float getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(Float averageScore) {
        this.averageScore = averageScore;
    }
}
