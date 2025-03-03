package com.triviktech.entities.krakpi;

import com.triviktech.entities.employee.EmployeeInformation;
import com.triviktech.entities.kra.KRA;
import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "kra_kpi")
public class KraKpi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Ensure this is set correctly
    @Column(name = "id", nullable = false)
    private Long kraKpiId;

    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "employee_id")
    private EmployeeInformation employeeInformation;

    @OneToMany(mappedBy = "kraKpi", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<KRA> kra;


    @Column(name = "remark", length = 1000)
    private String remark;

    @Column(name = "is_self_completed", nullable = false)
    private boolean selfCompleted ;  // Default value to avoid null

    @Column(name = "is_manager_completed", nullable = false)
    private boolean managerCompleted;

    public Long getKraKpiId() {
        return kraKpiId;
    }

    public void setKraKpiId(Long kraKpiId) {
        this.kraKpiId = kraKpiId;
    }

    public EmployeeInformation getEmployeeInformation() {
        return employeeInformation;
    }

    public void setEmployeeInformation(EmployeeInformation employeeInformation) {
        this.employeeInformation = employeeInformation;
    }

    public Set<KRA> getKra() {
        return kra;
    }

    public void setKra(Set<KRA> kra) {
        this.kra = kra;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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