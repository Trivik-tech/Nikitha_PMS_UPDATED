package com.triviktech.payloads.response.krakpi;

public class KraKpiDto {
    private String kra;
    private String kpi;
    private double weightage;
    private String employeeRemark;
    private String managerRemark;

    private double selfScore;
    private double managerScore;
    private double averageScore;

    public KraKpiDto() {
    }

    public KraKpiDto(String kra, String kpi, double weightage, String employeeRemark, String managerRemark,
                     double selfScore, double managerScore, double averageScore) {
        this.kra = kra;
        this.kpi = kpi;
        this.weightage = weightage;
        this.employeeRemark = employeeRemark;
        this.managerRemark = managerRemark;
        this.selfScore = selfScore;
        this.managerScore = managerScore;
        this.averageScore = averageScore;
    }

    public String getKra() {
        return kra;
    }

    public void setKra(String kra) {
        this.kra = kra;
    }

    public String getKpi() {
        return kpi;
    }

    public void setKpi(String kpi) {
        this.kpi = kpi;
    }

    public double getWeightage() {
        return weightage;
    }

    public void setWeightage(double weightage) {
        this.weightage = weightage;
    }

    public String getEmployeeRemark() {
        return employeeRemark;
    }

    public void setEmployeeRemark(String employeeRemark) {
        this.employeeRemark = employeeRemark;
    }

    public String getManagerRemark() {
        return managerRemark;
    }

    public void setManagerRemark(String managerRemark) {
        this.managerRemark = managerRemark;
    }

    public double getSelfScore() {
        return selfScore;
    }

    public void setSelfScore(double selfScore) {
        this.selfScore = selfScore;
    }

    public double getManagerScore() {
        return managerScore;
    }

    public void setManagerScore(double managerScore) {
        this.managerScore = managerScore;
    }

    public double getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(double averageScore) {
        this.averageScore = averageScore;
    }
}
