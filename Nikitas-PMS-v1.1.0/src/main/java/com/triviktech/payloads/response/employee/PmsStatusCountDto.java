package com.triviktech.payloads.response.employee;

/**
 * Data Transfer Object (DTO) representing the status counts of PMS (Performance Management System).
 * <p>
 * This object is typically used in PMS dashboards or reports to show
 * the number of employees who have completed their reviews versus those
 * whose reviews are still pending.
 * </p>
 *
 * <h3>Fields:</h3>
 * <ul>
 *   <li>{@link #completedCount} - Number of employees who have completed their PMS reviews.</li>
 *   <li>{@link #pendingCount} - Number of employees whose PMS reviews are still pending.</li>
 * </ul>
 */

public class PmsStatusCountDto {
    private long completedCount;
    private long pendingCount;

    public PmsStatusCountDto() {}

    public PmsStatusCountDto(long completedCount, long pendingCount) {
        this.completedCount = completedCount;
        this.pendingCount = pendingCount;
    }

    public long getCompletedCount() {
        return completedCount;
    }

    public void setCompletedCount(long completedCount) {
        this.completedCount = completedCount;
    }

    public long getPendingCount() {
        return pendingCount;
    }

    public void setPendingCount(long pendingCount) {
        this.pendingCount = pendingCount;
    }
}
