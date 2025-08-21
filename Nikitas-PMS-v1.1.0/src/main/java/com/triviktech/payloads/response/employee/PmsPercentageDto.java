package com.triviktech.payloads.response.employee;

/**
 * Data Transfer Object (DTO) that represents the percentage distribution of PMS (Performance
 * Management System) statuses.
 * <p>
 * This class is immutable since its fields are declared {@code final} and
 * initialized only once through the constructor. It is mainly used to send PMS
 * completion statistics (completed vs. pending percentages) in API responses.
 */

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