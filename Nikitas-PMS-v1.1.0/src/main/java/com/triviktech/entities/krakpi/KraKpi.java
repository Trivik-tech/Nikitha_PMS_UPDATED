package com.triviktech.entities.krakpi;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.triviktech.entities.employee.EmployeeInformation;
import com.triviktech.entities.kra.KRA;
import com.triviktech.entities.manager.Manager;
import jakarta.persistence.*;

import java.util.Date;
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

    @Column(name = "review_2")
    private Boolean review2;


    @Column(name = "due_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private Date dueDate;


    @Column(name = "self_review_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private Date selfReviewDate;


    @Column(name = "manager_review_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private Date managerReviewDate;

    @Column(name = "pms_initiated")
    private Boolean pmsInitiated;

    @Column(name = "manager_approval")
    private Boolean managerApproval;

    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "manager_manager_id")
    private Manager manager;

    public Manager getManager() {
        return manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }

    public Boolean getManagerApproval() {
        return managerApproval;
    }

    public void setManagerApproval(Boolean managerApproval) {
        this.managerApproval = managerApproval;
    }

    public Boolean getPmsInitiated() {
        return pmsInitiated;
    }

    public void setPmsInitiated(Boolean pmsInitiated) {
        this.pmsInitiated = pmsInitiated;
    }

    public Date getManagerReviewDate() {
        return managerReviewDate;
    }

    public void setManagerReviewDate(Date managerReviewDate) {
        this.managerReviewDate = managerReviewDate;
    }

    public Date getSelfReviewDate() {
        return selfReviewDate;
    }

    public void setSelfReviewDate(Date selfReviewDate) {
        this.selfReviewDate = selfReviewDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Boolean getReview2() {
        return review2;
    }

    public void setReview2(Boolean review2) {
        this.review2 = review2;
    }

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