package com.triviktech.payloads.response.employee;

public class PmsPercentageDto {
    private final double completedPercentage;
    private final double pendingPercentage;

    public PmsPercentageDto(double completedPercentage, double pendingPercentage) {
        this.completedPercentage = completedPercentage;
        this.pendingPercentage = pendingPercentage;
    }

    public double getCompletedPercentage() {
        return completedPercentage;
    }

    public double getPendingPercentage() {
        return pendingPercentage;
    }
}